package me.ooify.pdi.service.tool;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.MimeTypeUtils;
import org.springframework.web.multipart.MultipartFile;

@Service
public class OCRService {
    private static final String DEFAULT_PROMPT = """
            请从图片中提取以下字段信息，并严格按照下方 JSON 模板格式返回结果。
            要求：
            1. 仅输出 JSON，不要包含多余文字。
            2. 当无法识别某字段时，保留键名，值设为空字符串 ""。
            3. 请确保 JSON 结构完整且符合标准格式。
            
            JSON 模板如下：
            {
              "task_name": "",
              "inspection_location": "",
              "inspection_date": "",
              "start_manhole_id_end_manhole_id": "",
              "inspection_direction": "",
              "pipe_material": "",
              "pipe_diameter": "",
              "pipeline_type": "",
              "inspection_unit": "",
              "inspector": ""
            }
            """;

    @Autowired
    private ChatClient chatClient;

    private String OCRChat(MultipartFile image) {
        return chatClient.prompt()
                .user(p -> p.text(DEFAULT_PROMPT).media(MimeTypeUtils.IMAGE_PNG, image.getResource()))
                .call()
                .content();
    }
}
