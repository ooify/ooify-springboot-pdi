package me.ooify.pdi.controller;

import java.util.List;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import me.ooify.common.annotation.Log;
import me.ooify.common.core.controller.BaseController;
import me.ooify.common.core.domain.AjaxResult;
import me.ooify.common.enums.BusinessType;
import me.ooify.pdi.domain.PipeVideo;
import me.ooify.pdi.service.IPipeVideoService;
import me.ooify.common.utils.poi.ExcelUtil;
import me.ooify.common.core.page.TableDataInfo;

/**
 * 管道视频Controller
 * 
 * @author ooify
 * @date 2025-11-03
 */
@RestController
@RequestMapping("/pdi/video")
public class PipeVideoController extends BaseController
{
    @Autowired
    private IPipeVideoService pipeVideoService;

    /**
     * 查询管道视频列表
     */
    @PreAuthorize("@ss.hasPermi('pdi:video:list')")
    @GetMapping("/list")
    public TableDataInfo list(PipeVideo pipeVideo)
    {
        startPage();
        List<PipeVideo> list = pipeVideoService.selectPipeVideoList(pipeVideo);
        return getDataTable(list);
    }

    /**
     * 导出管道视频列表
     */
    @PreAuthorize("@ss.hasPermi('pdi:video:export')")
    @Log(title = "管道视频", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, PipeVideo pipeVideo)
    {
        List<PipeVideo> list = pipeVideoService.selectPipeVideoList(pipeVideo);
        ExcelUtil<PipeVideo> util = new ExcelUtil<PipeVideo>(PipeVideo.class);
        util.exportExcel(response, list, "管道视频数据");
    }

    /**
     * 获取管道视频详细信息
     */
    @PreAuthorize("@ss.hasPermi('pdi:video:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return success(pipeVideoService.selectPipeVideoById(id));
    }

    /**
     * 新增管道视频
     */
    @PreAuthorize("@ss.hasPermi('pdi:video:add')")
    @Log(title = "管道视频", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody PipeVideo pipeVideo)
    {
        return toAjax(pipeVideoService.insertPipeVideo(pipeVideo));
    }

    /**
     * 修改管道视频
     */
    @PreAuthorize("@ss.hasPermi('pdi:video:edit')")
    @Log(title = "管道视频", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody PipeVideo pipeVideo)
    {
        return toAjax(pipeVideoService.updatePipeVideo(pipeVideo));
    }

    /**
     * 删除管道视频
     */
    @PreAuthorize("@ss.hasPermi('pdi:video:remove')")
    @Log(title = "管道视频", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(pipeVideoService.deletePipeVideoByIds(ids));
    }
}
