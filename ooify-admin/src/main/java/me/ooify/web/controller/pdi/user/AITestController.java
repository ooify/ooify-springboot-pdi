package me.ooify.web.controller.pdi.user;

import com.alibaba.fastjson2.JSONObject;
import me.ooify.common.annotation.Anonymous;
import me.ooify.common.core.controller.BaseController;
import me.ooify.common.core.domain.AjaxResult;
import me.ooify.common.core.page.TableDataInfo;
import me.ooify.common.utils.SecurityUtils;
import me.ooify.pdi.domain.vo.PipVideoTableVO;
import me.ooify.pdi.service.IPdiService;
import me.ooify.pdi.service.IPipeVideoService;
import me.ooify.pdi.service.impl.PdiServiceImpl;
import me.ooify.pdi.service.tool.MessagingService;
import me.ooify.pdi.service.tool.OCRService;
import me.ooify.pdi.service.tool.OSSService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/ai")
public class AITestController extends BaseController {
    @Autowired
    private MessagingService messagingService;

    @Autowired
    private IPdiService pdiService;

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

    @Anonymous
    @PostMapping("/ocr_distance")
    public AjaxResult ocrDistanceTest(@RequestParam("url") String url) throws Exception {
        String s = ocrService.PipDistanceOcr(url);
        JSONObject jsonObject = JSONObject.parseObject(s);
        return AjaxResult.success(jsonObject);
    }

    @GetMapping("/get_post_signature_for_oss_upload")
    public Map<String, String> getPostSignatureForOssUpload() throws Exception {
        return ossService.getPostSignatureForOssUpload(1L);
    }

    @PostMapping("/test_video_status")
    public TableDataInfo testVideoStatus(@RequestBody PipVideoTableVO pipVideoTableVO) {
        startPage();
        pipVideoTableVO.setCreateBy(SecurityUtils.getUsername());
        List<PipVideoTableVO> pipVideoTableVOS = pdiService.selectPipVideoTableList(pipVideoTableVO);
        return getDataTable(pipVideoTableVOS);
    }
}
