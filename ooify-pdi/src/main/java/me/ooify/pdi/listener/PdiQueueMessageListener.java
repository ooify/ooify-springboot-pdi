package me.ooify.pdi.listener;

import com.alibaba.fastjson2.JSONObject;

import me.ooify.pdi.config.RabbitMQConfig;
import me.ooify.pdi.domain.PipeVideo;
import me.ooify.pdi.service.IPipeVideoService;
import me.ooify.pdi.service.tool.OCRService;
import me.ooify.pdi.service.tool.WebSocketServer;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class PdiQueueMessageListener {
    @Autowired
    private OCRService ocrService;
    @Autowired
    private IPipeVideoService pipeVideoService;
    @Autowired
    private WebSocketServer webSocketServer;
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
            webSocketServer.sendToUser(userId,"ocr-update:" + videoId);
        }
    }

    // 文档生成消费者
    @Component
    @RabbitListener(queues = RabbitMQConfig.DOC_QUEUE)
    public class DocConsumer {
        @RabbitHandler
        public void process(String message) {
            System.out.println("生成文档任务: " + message);
        }
    }


}
