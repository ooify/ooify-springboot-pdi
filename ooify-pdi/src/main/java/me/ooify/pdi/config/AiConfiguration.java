package me.ooify.pdi.config;

import com.alibaba.cloud.ai.dashscope.chat.DashScopeChatModel;
import com.alibaba.cloud.ai.dashscope.chat.DashScopeChatOptions;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AiConfiguration {

    @Bean
    public ChatClient dashScopeChatClient(DashScopeChatModel model) {
        return ChatClient.builder(model)
                .defaultOptions(DashScopeChatOptions.builder()
                        .withModel("qwen3-vl-flash")
//                        .withModel("qwen3-vl-plus")
                        .withMultiModel(true)  // 多模态支持
                        .build())
                .build();
    }
}
