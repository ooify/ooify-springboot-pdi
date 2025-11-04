package me.ooify.web.controller.pdi.user;

import me.ooify.common.annotation.Log;
import me.ooify.common.core.controller.BaseController;
import me.ooify.common.core.domain.AjaxResult;
import me.ooify.common.core.page.TableDataInfo;
import me.ooify.common.enums.BusinessType;
import me.ooify.common.utils.SecurityUtils;
import me.ooify.pdi.domain.PipeVideo;
import me.ooify.pdi.domain.vo.PipVideoVO;
import me.ooify.pdi.service.IPdiService;
import me.ooify.pdi.service.IPipeVideoService;
import me.ooify.system.service.ISysUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        PipeVideo pipeVideo = pipeVideoService.selectPipeVideoById(id);
        PipVideoVO pipVideoVO = pdiService.selectPipeVideoVOById(id);
        if (!SecurityUtils.getUsername().equals(pipeVideo.getCreateBy())) {
            return AjaxResult.error("没有权限访问该视频");
        }
        return success(pipVideoVO);
    }


    /**
     * 上传管道视频
     */
    @PreAuthorize("@ss.hasRole('user')")
    @PostMapping
    public AjaxResult upVideo(@RequestBody PipeVideo pipeVideo)
    {
        return toAjax(pipeVideoService.insertPipeVideo(pipeVideo));
    }
//
//    /**
//     * 新增管道视频
//     */
//    @PreAuthorize("@ss.hasRole('user')")
//    @Log(title = "管道视频", businessType = BusinessType.INSERT)
//    @PostMapping
//    public AjaxResult add(@RequestBody PipeVideo pipeVideo)
//    {
//        return toAjax(pipeVideoService.insertPipeVideo(pipeVideo));
//    }
//
//    /**
//     * 修改管道视频
//     */
//    @PreAuthorize("@ss.hasRole('user')")
//    @Log(title = "管道视频", businessType = BusinessType.UPDATE)
//    @PutMapping
//    public AjaxResult edit(@RequestBody PipeVideo pipeVideo)
//    {
//        return toAjax(pipeVideoService.updatePipeVideo(pipeVideo));
//    }
//
//    /**
//     * 删除管道视频
//     */
//    @PreAuthorize("@ss.hasRole('user')")
//    @Log(title = "管道视频", businessType = BusinessType.DELETE)
//	@DeleteMapping("/{ids}")
//    public AjaxResult remove(@PathVariable Long[] ids)
//    {
//        return toAjax(pipeVideoService.deletePipeVideoByIds(ids));
//    }
}