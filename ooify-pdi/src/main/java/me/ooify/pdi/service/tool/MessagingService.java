package me.ooify.pdi.service.tool;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MessagingService {
    @Autowired
    RabbitTemplate rabbitTemplate;

    public void sendPdiMessage(String msg) {
        rabbitTemplate.convertAndSend("pdi", "", msg);
    }
}
