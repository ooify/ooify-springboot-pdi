package me.ooify.web.controller.pdi.admin;

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
import me.ooify.pdi.domain.Road;
import me.ooify.pdi.service.IRoadService;
import me.ooify.common.utils.poi.ExcelUtil;
import me.ooify.common.core.page.TableDataInfo;

/**
 * 道路基础信息Controller
 * 
 * @author ooify
 * @date 2025-11-25
 */
@RestController
@RequestMapping("/pdi/road")
public class RoadController extends BaseController
{
    @Autowired
    private IRoadService roadService;

    /**
     * 查询道路基础信息列表
     */
    @PreAuthorize("@ss.hasPermi('pdi:road:list')")
    @GetMapping("/list")
    public TableDataInfo list(Road road)
    {
        startPage();
        List<Road> list = roadService.selectRoadList(road);
        return getDataTable(list);
    }

    /**
     * 导出道路基础信息列表
     */
    @PreAuthorize("@ss.hasPermi('pdi:road:export')")
    @Log(title = "道路基础信息", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, Road road)
    {
        List<Road> list = roadService.selectRoadList(road);
        ExcelUtil<Road> util = new ExcelUtil<Road>(Road.class);
        util.exportExcel(response, list, "道路基础信息数据");
    }

    /**
     * 获取道路基础信息详细信息
     */
    @PreAuthorize("@ss.hasPermi('pdi:road:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return success(roadService.selectRoadById(id));
    }

    /**
     * 新增道路基础信息
     */
    @PreAuthorize("@ss.hasPermi('pdi:road:add')")
    @Log(title = "道路基础信息", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody Road road)
    {
        return toAjax(roadService.insertRoad(road));
    }

    /**
     * 修改道路基础信息
     */
    @PreAuthorize("@ss.hasPermi('pdi:road:edit')")
    @Log(title = "道路基础信息", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody Road road)
    {
        return toAjax(roadService.updateRoad(road));
    }

    /**
     * 删除道路基础信息
     */
    @PreAuthorize("@ss.hasPermi('pdi:road:remove')")
    @Log(title = "道路基础信息", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(roadService.deleteRoadByIds(ids));
    }
}
