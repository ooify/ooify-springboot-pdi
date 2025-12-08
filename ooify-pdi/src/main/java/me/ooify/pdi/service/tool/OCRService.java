package me.ooify.pdi.service.tool;

import com.alibaba.fastjson2.JSONObject;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.MimeTypeUtils;
import org.springframework.web.multipart.MultipartFile;

import java.net.MalformedURLException;

@Service
public class OCRService {
    private static final String PipInfo_PROMPT = """
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
    private static final String PipDistance_PROMPT = """
            请从图片中提取名为“距离”的字段信息，并严格按照下方 JSON 模板格式返回结果。
            要求：
            1. 仅输出 JSON，不要包含多余文字。
            2. 当无法识别某字段时，保留键名，值设为空字符串 ""。
            3. 请确保 JSON 结构完整且符合标准格式。
            4. “距离”字段的值应为数字，单位为米（m）(最后结果不包含单位字符)，另外小数点前最多为两位数字，保留所有小数，帮我正确划分小数点。
            JSON 模板如下：
            {
              "pipe_length": ""
            }
            """;

    @Autowired
    private ChatClient chatClient;


    public JSONObject PipInfoOcr(MultipartFile image) {
        String s = ocrChat(image, PipInfo_PROMPT);
        return JSONObject.parseObject(s);
    }

    public String PipDistanceOcr(String url) throws MalformedURLException {
        Resource resource = new UrlResource(url);
        return chatClient.prompt()
                .user(p -> p.text(PipDistance_PROMPT).media(MimeTypeUtils.IMAGE_PNG, resource))
                .call()
                .content();
    }

    public String PipInfoOcr(String url) throws MalformedURLException {
        Resource resource = new UrlResource(url);
        return chatClient.prompt()
                .user(p -> p.text(PipInfo_PROMPT).media(MimeTypeUtils.IMAGE_PNG, resource))
                .call()
                .content();
    }

    public String ocrChat(MultipartFile image, String prompt) {
        return chatClient.prompt()
                .user(p -> p.text(prompt).media(MimeTypeUtils.IMAGE_PNG, image.getResource()))
                .call()
                .content();
    }
}
