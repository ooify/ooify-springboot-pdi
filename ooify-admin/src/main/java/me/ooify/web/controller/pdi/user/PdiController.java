package me.ooify.web.controller.pdi.user;

import com.alibaba.fastjson2.JSONObject;
import me.ooify.common.core.controller.BaseController;
import me.ooify.common.core.domain.AjaxResult;
import me.ooify.common.core.page.TableDataInfo;
import me.ooify.common.utils.SecurityUtils;
import me.ooify.pdi.domain.PipeVideo;
import me.ooify.pdi.domain.vo.PipVideoVO;
import me.ooify.pdi.service.IPdiService;
import me.ooify.pdi.service.IPipeVideoService;
import me.ooify.pdi.service.tool.MessagingService;
import me.ooify.pdi.service.tool.OCRService;
import me.ooify.pdi.service.tool.OSSService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

/**
 * 用户上传视频Controller
 *
 * @author ooify
 * @date 2025-11-03
 */
@RestController
@RequestMapping("/user/pdi/video")
public class PdiController extends BaseController {
    @Autowired
    private IPipeVideoService pipeVideoService;

    @Autowired
    private IPdiService pdiService;

    @Autowired
    private MessagingService messagingService;

    @Autowired
    private OSSService ossService;

    /**
     * 查询用户视频列表
     */
    @PreAuthorize("@ss.hasRole('user')")
    @GetMapping("/list")
    public TableDataInfo list(PipVideoVO pipVideoVO) {
        startPage();
        List<PipVideoVO> list = pdiService.selectPipeVideoVOListByUser(pipVideoVO, SecurityUtils.getUsername());
        return getDataTable(list);
    }


    /**
     * 获取用户管道视频详细信息
     */
    @PreAuthorize("@ss.hasRole('user')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id) {
        PipeVideo pipeVideo = pipeVideoService.selectPipeVideoById(id);
        PipVideoVO pipVideoVO = pdiService.selectPipeVideoVOById(id);
        if (!SecurityUtils.getUsername().equals(pipeVideo.getCreateBy())) {
            return AjaxResult.error("没有权限访问该视频");
        }
        return success(pipVideoVO);
    }

    /**
     * 新增管道视频
     */
    @PreAuthorize("@ss.hasRole('user')")
    @PostMapping(value = "/add", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public AjaxResult add(@RequestParam("pipeVideo") String pipeVideoStr,
                          @RequestParam("image") MultipartFile image
    ) throws Exception {
        PipVideoVO pipVideoVO = JSONObject.parseObject(pipeVideoStr, PipVideoVO.class);
        if (image == null || image.isEmpty()) {
            return AjaxResult.error("上传的图片不能为空");
        }
        Map<String, String> signature = pdiService.handleUserUpload(pipVideoVO, image);

        return success(signature);
    }


    /**
     * 修改管道视频
     */
    @PreAuthorize("@ss.hasRole('user')")
    @PutMapping
    public AjaxResult edit(@RequestBody PipVideoVO pipVideoVO) {
        PipeVideo pipeVideo = new PipeVideo();
        BeanUtils.copyProperties(pipVideoVO, pipeVideo);
        return toAjax(pipeVideoService.updatePipeVideo(pipeVideo));
    }

    /**
     * 删除管道视频
     */
    @PreAuthorize("@ss.hasRole('user')")
    @DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids) {
        return toAjax(pipeVideoService.deletePipeVideoByIds(ids));
    }
}