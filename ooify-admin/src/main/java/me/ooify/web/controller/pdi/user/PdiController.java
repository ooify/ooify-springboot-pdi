package me.ooify.web.controller.pdi.user;

import me.ooify.common.annotation.Anonymous;
import me.ooify.common.core.controller.BaseController;
import me.ooify.common.core.domain.AjaxResult;
import me.ooify.common.core.page.TableDataInfo;
import me.ooify.common.utils.SecurityUtils;
import me.ooify.pdi.domain.PipeVideo;
import me.ooify.pdi.domain.vo.PipVideoVO;
import me.ooify.pdi.service.IPdiService;
import me.ooify.pdi.service.IPipeVideoService;
import me.ooify.pdi.service.tool.OSSService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 用户管道识别Controller
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
        if (!SecurityUtils.getUsername().equals(pipeVideo.getCreateBy())) {
            return AjaxResult.error("没有权限访问该视频");
        }
        PipVideoVO pipVideoVO = pdiService.selectPipeVideoVOById(id);
        return success(pipVideoVO);
    }

    /**
     * 新增管道视频
     */
    @PreAuthorize("@ss.hasRole('user')")
    @PostMapping()
    public AjaxResult add(@RequestBody PipVideoVO pipVideoVO) throws Exception {
        System.out.println(pipVideoVO);
        Map<String, String> signature = pdiService.handleUserUpload(pipVideoVO);
        return success(signature);
    }

    /**
     * 上传管道视频回调
     */
//    @PreAuthorize("@ss.hasRole('user')")
    @Anonymous
    @PostMapping("/upload_callback")
    public AjaxResult uploadCallback(@RequestParam("fileId") Long videoId,
                                     @RequestParam("url") String videoUrl,
                                     @RequestParam("userId") Long userId) {
//        System.out.println("上传回调: videoId=" + videoId + ", videoUrl=" + videoUrl);
        pdiService.handleUploadCallback(videoId, videoUrl, userId);
        return success();
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
     * 重新上传视频
     */
    @PreAuthorize("@ss.hasRole('user')")
    @PostMapping("/reupload/{id}")
    public AjaxResult reupload(@PathVariable("id") Long videoId) throws Exception {
        Map<String, String> postSignatureForOssUpload = ossService.getPostSignatureForOssUpload(videoId);
        return success(postSignatureForOssUpload);
    }

    /**
     * 管道视频上传失败
     */
    @PreAuthorize("@ss.hasRole('user')")
    @PostMapping("/upload_failed")
    public AjaxResult uploadFailed(@RequestParam("fileId") Long videoId,
                                   @RequestParam("upload_error") String upload_error) {
        PipeVideo pipeVideo = new PipeVideo();
        pipeVideo.setId(videoId);
        pipeVideo.setUploadError(upload_error);
//        上传失败
        pipeVideo.setUploadStatus(2L);
        pipeVideoService.updatePipeVideo(pipeVideo);
        return success();
    }

    /**
     * 确认管道信息
     */
    @PreAuthorize("@ss.hasRole('user')")
    @PostMapping("/confirm_pipe_info/{id}")
    public AjaxResult confirmPipeInfo(@PathVariable("id") Long videoId,
                                      @RequestBody String pipInfo) {
        PipeVideo pipeVideo = new PipeVideo();
        pipeVideo.setId(videoId);
        pipeVideo.setPipeInfo(pipInfo);
//        确认完成
        pipeVideo.setUploadStatus(1L);
        pipeVideoService.updatePipeVideo(pipeVideo);
        return success();
    }

    /**
     * 删除管道视频
     */
    @PreAuthorize("@ss.hasRole('user')")
    @DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids) {
        return toAjax(pdiService.deletePipeVideoByIds(ids));
    }
}