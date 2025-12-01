package me.ooify.pdi.listener;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;

import me.ooify.common.utils.SecurityUtils;
import me.ooify.pdi.config.RabbitMQConfig;
import me.ooify.pdi.domain.PipeVideo;
import me.ooify.pdi.domain.ReportTask;
import me.ooify.pdi.service.IPipeVideoService;
import me.ooify.pdi.service.IReportTaskService;
import me.ooify.pdi.service.tool.OCRService;
import me.ooify.pdi.service.tool.WebSocketServer;
import me.ooify.pdi.utils.file.AliOssUtil;
import okhttp3.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Component
public class PdiQueueMessageListener {

    @Autowired
    private OCRService ocrService;

    @Autowired
    private IPipeVideoService pipeVideoService;

    @Autowired
    private WebSocketServer webSocketServer;

    @Autowired
    private IReportTaskService reportTaskService;

    @Autowired
    private AliOssUtil aliOssUtil;


    // OCR 消费者
    @Component
    @RabbitListener(queues = RabbitMQConfig.OCR_QUEUE)
    public class OCRConsumer {
        @RabbitHandler
        public void process(String message) throws Exception {
            JSONObject json = JSONObject.parseObject(message);
            Long videoId = json.getLong("videoId");
            String url = json.getString("url");
            Long userId = json.getLong("userId");

            String s = ocrService.PipInfoOcr(url);
            JSONObject jsonObject = JSONObject.parseObject(s);
            PipeVideo pipeVideo = new PipeVideo();
            pipeVideo.setId((videoId));
            pipeVideo.setPipeInfo(String.valueOf(jsonObject));
            pipeVideo.setUploadStatus(3L);
            pipeVideoService.updatePipeVideo(pipeVideo);
//            前端消息推送
            Map<String, Object> msg = new HashMap<>();
            msg.put("type", "video-upload");
            msg.put("videoId", videoId);
            webSocketServer.sendToUser(userId, JSON.toJSONString(msg));
        }
    }

    // 文档生成消费者
    @Component
    @RabbitListener(queues = RabbitMQConfig.DOC_QUEUE)
    public class DocConsumer {
        private static final Logger logger = LoggerFactory.getLogger(DocConsumer.class);

        @RabbitHandler
        public void process(String message) throws IOException {
            JSONObject json = JSONObject.parseObject(message);
            Long videoId = json.getLong("videoId");
            Long userId = json.getLong("userId");
            Long taskId = json.getLong("taskId");

            // 更新任务状态为处理中
            ReportTask reportTask = new ReportTask();
            reportTask.setId(taskId);
            reportTask.setTaskStatus(1L);
            reportTask.setStartTime(new java.util.Date());
            reportTaskService.updateReportTask(reportTask);

            // 前端消息推送
            Map<String, Object> msg = new HashMap<>();
            msg.put("type", "task-update");
            msg.put("taskId", taskId);
            webSocketServer.sendToUser(userId, JSON.toJSONString(msg));

            // 处理pipInfo字段，确保它是JSON对象而不是字符串
            Object pipInfo = json.get("pipInfo");
            if (pipInfo instanceof String) {
                json.put("pipInfo", JSONObject.parseObject((String) pipInfo));
            }

            json.remove("videoId");
            json.remove("taskId");
            json.remove("userId");

            String jsonData = json.toJSONString();
            logger.info("发送给文档生成接口的数据：{}", jsonData);

            OkHttpClient client = new OkHttpClient.Builder()
                    .connectTimeout(30, TimeUnit.SECONDS)
                    .readTimeout(60, TimeUnit.SECONDS)
                    .writeTimeout(30, TimeUnit.SECONDS)
                    .build();

            Request request = new Request.Builder()
//                    .url("http://ip:port/api/analyze_complete")
                    .post(RequestBody.create(jsonData, MediaType.parse("application/json")))
                    .build();

            try (Response response = client.newCall(request).execute()) {
                if (!response.isSuccessful()) {
                    String errorBody = response.body() != null ? response.body().string() : "无响应体";
                    logger.error("文档生成接口请求失败，状态码：{}，错误信息：{}", response.code(), errorBody);
                    throw new RuntimeException("请求失败：" + response.code() + "，错误信息：" + errorBody);
                }

                // 获取响应头中的文件名
                String contentDisposition = response.header("Content-Disposition");
                String filename = "report_" + taskId + "_" + System.currentTimeMillis() + ".docx";

                if (contentDisposition != null && contentDisposition.contains("filename=")) {
                    String headerFilename = contentDisposition.split("filename=")[1].replace("\"", "");
                    if (headerFilename != null && !headerFilename.trim().isEmpty()) {
                        filename = headerFilename;
                    }
                }

                // 获取文件二进制内容
                byte[] fileBytes = response.body().bytes();

                // 转为 MultipartFile（用于OSS上传函数）
                MultipartFile multipartFile = new MockMultipartFile(
                        filename,
                        filename,
                        "application/vnd.openxmlformats-officedocument.wordprocessingml.document",
                        fileBytes
                );

                // 上传到 OSS
                String directory = "doc/";
                String ossUrl = aliOssUtil.upload(multipartFile, filename, directory);
                logger.info("文件已上传到 OSS，访问 URL：{}", ossUrl);

                // 更新任务状态为完成
                ReportTask finish = new ReportTask();
                finish.setId(taskId);
                finish.setTaskStatus(2L);
                finish.setEndTime(new java.util.Date());
                finish.setReportUrl(ossUrl);
                reportTaskService.updateReportTask(finish);

                // 通知前端任务完成
                msg.put("type", "task-update");
                msg.put("taskId", taskId);
//                msg.put("reportUrl", ossUrl);
                webSocketServer.sendToUser(userId, JSON.toJSONString(msg));

                logger.info("文档生成任务完成，taskId: {}, OSS URL: {}", taskId, ossUrl);

            } catch (Exception e) {
                logger.error("文档生成任务处理失败，taskId: {}", taskId, e);

                // 更新任务状态为失败
                ReportTask failedTask = new ReportTask();
                failedTask.setId(taskId);
                failedTask.setTaskStatus(3L);
                failedTask.setEndTime(new java.util.Date());
                failedTask.setRemark("文档生成失败: " + e.getMessage());
                reportTaskService.updateReportTask(failedTask);

                // 通知前端任务失败
                msg.put("type", "task-update");
                msg.put("taskId", taskId);
//                msg.put("error", "文档生成失败，请稍后重试");
                webSocketServer.sendToUser(userId, JSON.toJSONString(msg));

                throw new RuntimeException("文档生成任务处理失败", e);
            }
        }
    }


}
