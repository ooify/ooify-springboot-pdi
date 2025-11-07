package me.ooify.pdi.listener;

import com.alibaba.fastjson2.JSONObject;

import java.util.HashMap;

import me.ooify.pdi.config.RabbitMQConfig;
import me.ooify.pdi.domain.PipeVideo;
import me.ooify.pdi.service.IPipeVideoService;
import me.ooify.pdi.service.tool.OCRService;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

@Component
public class PdiQueueMessageListener {
    @Autowired
    private OCRService ocrService;
    @Autowired
    private IPipeVideoService pipeVideoService;

    // OCR 消费者
    @Component
    @RabbitListener(queues = RabbitMQConfig.OCR_QUEUE)
    public class OCRConsumer {
        @RabbitHandler
        public void process(String message) throws Exception {
            JSONObject json = JSONObject.parseObject(message);
            Long videoId = json.getLong("videoId");
            String url = json.getString("url");

            String s = ocrService.PipInfoOcr(url);
            JSONObject jsonObject = JSONObject.parseObject(s);
            PipeVideo pipeVideo = new PipeVideo();
            pipeVideo.setId((videoId));
            pipeVideo.setPipeInfo(String.valueOf(jsonObject));
            pipeVideoService.updatePipeVideo(pipeVideo);
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
