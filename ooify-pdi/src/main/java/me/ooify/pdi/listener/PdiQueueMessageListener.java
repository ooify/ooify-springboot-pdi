package me.ooify.pdi.listener;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;

import me.ooify.pdi.config.RabbitMQConfig;
import me.ooify.pdi.domain.PipeVideo;
import me.ooify.pdi.domain.ReportTask;
import me.ooify.pdi.service.IPipeVideoService;
import me.ooify.pdi.service.IReportTaskService;
import me.ooify.pdi.service.tool.OCRService;
import me.ooify.pdi.service.tool.WebSocketServer;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

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
        @RabbitHandler
        public void process(String message) throws InterruptedException {
            JSONObject json = JSONObject.parseObject(message);
            Long userId = json.getLong("userId");
            Long taskId = json.getLong("taskId");
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

            Thread.sleep(2 * 60 * 1000);


//            上传到oss
//            return url, public String upload(MultipartFile file, String filename, String directory)


            ReportTask finish = new ReportTask();
            finish.setId(taskId);
            finish.setTaskStatus(2L);
            finish.setEndTime(new java.util.Date());
            finish.setReportUrl("");
            reportTaskService.updateReportTask(finish);
            webSocketServer.sendToUser(userId, JSON.toJSONString(msg));

        }
    }


}
