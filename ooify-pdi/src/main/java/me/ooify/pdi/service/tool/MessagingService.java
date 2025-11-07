package me.ooify.pdi.service.tool;

import com.alibaba.fastjson2.JSONObject;
import me.ooify.pdi.config.RabbitMQConfig;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Base64;
import java.util.HashMap;

@Service
public class MessagingService {
    @Autowired
    RabbitTemplate rabbitTemplate;

    public void sendDocMessage(String msg) {
        rabbitTemplate.convertAndSend(
                RabbitMQConfig.DOC_EXCHANGE,
                RabbitMQConfig.DOC_ROUTING_KEY,
                msg);
    }

    public void sendOCRMessage(Long pipeVideoId, String url) {
        JSONObject message = new JSONObject();
        message.put("videoId", pipeVideoId);
        message.put("url", url);

        rabbitTemplate.convertAndSend(
                RabbitMQConfig.OCR_EXCHANGE,
                RabbitMQConfig.OCR_ROUTING_KEY,
                message.toJSONString()
        );
    }

}
