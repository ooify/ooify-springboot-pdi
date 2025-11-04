package me.ooify.pdi.listener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class PdiQueueMessageListener {

    final Logger logger = LoggerFactory.getLogger(getClass());

    static final String QUEUE_TASK = "doc_task";

    @RabbitListener(queues = QUEUE_TASK)
    public void onRegistrationMessageFromMailQueue(String message) {
        logger.info("queue {} received registration message: {}", QUEUE_TASK, message);
//        logger.info("queue {} received task: {}", QUEUE_TASK, message);
//        try {
//            // 模拟任务执行耗时20秒
//            Thread.sleep(20000);
//            logger.info("queue {} finished processing task: {}", QUEUE_TASK, message);
//        } catch (InterruptedException e) {
//            Thread.currentThread().interrupt();
//            logger.error("Task interrupted while processing message: {}", message, e);
//        }
    }
}
