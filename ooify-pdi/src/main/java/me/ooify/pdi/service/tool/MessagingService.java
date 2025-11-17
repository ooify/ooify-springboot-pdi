package me.ooify.pdi.service.tool;

import com.alibaba.fastjson2.JSONObject;
import me.ooify.pdi.config.RabbitMQConfig;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

    public void sendOCRMessage(Long pipeVideoId, String url, Long userId) {
        JSONObject message = new JSONObject();
        message.put("videoId", pipeVideoId);
        message.put("url", url);
        message.put("userId", userId);
        rabbitTemplate.convertAndSend(
                RabbitMQConfig.OCR_EXCHANGE,
                RabbitMQConfig.OCR_ROUTING_KEY,
                message.toJSONString()
        );
    }

}
