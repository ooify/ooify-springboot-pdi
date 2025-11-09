package me.ooify.web.controller.pdi.user;

import com.alibaba.fastjson2.JSONObject;
import me.ooify.common.core.domain.AjaxResult;
import me.ooify.pdi.service.tool.MessagingService;
import me.ooify.pdi.service.tool.OCRService;
import me.ooify.pdi.service.tool.OSSService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/ai")
public class AITestController {
    @Autowired
    private MessagingService messagingService;

    @Autowired
    private OCRService ocrService;

    @Autowired
    private OSSService ossService;

    @GetMapping("/test")
    public AjaxResult test(String msg) {
//        messagingService.sendPdiMessage(msg);
        return AjaxResult.success();
    }

    @PostMapping("/ocr")
    public AjaxResult ocrTest(@RequestParam("image") MultipartFile image) {
        JSONObject jsonObject = ocrService.PipInfoOcr(image);
        return AjaxResult.success(jsonObject);
    }
    @GetMapping("/get_post_signature_for_oss_upload")
    public Map<String,String> getPostSignatureForOssUpload() throws Exception {
        return ossService.getPostSignatureForOssUpload(1L);
    }
}
