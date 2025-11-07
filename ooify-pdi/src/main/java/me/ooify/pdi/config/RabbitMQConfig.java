package me.ooify.pdi.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    // OCR 队列
    public static final String OCR_EXCHANGE = "ocr_exchange";
    public static final String OCR_QUEUE = "ocr_queue";
    public static final String OCR_ROUTING_KEY = "ocr_task";

    // 文档生成队列
    public static final String DOC_EXCHANGE = "doc_exchange";
    public static final String DOC_QUEUE = "doc_queue";
    public static final String DOC_ROUTING_KEY = "doc_task";

    // OCR配置
    @Bean
    public DirectExchange ocrExchange() {
        return new DirectExchange(OCR_EXCHANGE);
    }

    @Bean
    public Queue ocrQueue() {
        return new Queue(OCR_QUEUE);
    }

    @Bean
    public Binding ocrBinding() {
        return BindingBuilder.bind(ocrQueue())
                .to(ocrExchange())
                .with(OCR_ROUTING_KEY);
    }

    // 文档生成配置
    @Bean
    public DirectExchange docExchange() {
        return new DirectExchange(DOC_EXCHANGE);
    }

    @Bean
    public Queue docQueue() {
        return new Queue(DOC_QUEUE);
    }

    @Bean
    public Binding docBinding() {
        return BindingBuilder.bind(docQueue())
                .to(docExchange())
                .with(DOC_ROUTING_KEY);
    }
}