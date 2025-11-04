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
import me.ooify.pdi.domain.ReportTask;
import me.ooify.pdi.service.IReportTaskService;
import me.ooify.common.utils.poi.ExcelUtil;
import me.ooify.common.core.page.TableDataInfo;

/**
 * 报告生成任务Controller
 * 
 * @author ooify
 * @date 2025-11-03
 */
@RestController
@RequestMapping("/pdi/task")
public class ReportTaskController extends BaseController
{
    @Autowired
    private IReportTaskService reportTaskService;

    /**
     * 查询报告生成任务列表
     */
    @PreAuthorize("@ss.hasPermi('pdi:task:list')")
    @GetMapping("/list")
    public TableDataInfo list(ReportTask reportTask)
    {
        startPage();
        List<ReportTask> list = reportTaskService.selectReportTaskList(reportTask);
        return getDataTable(list);
    }

    /**
     * 导出报告生成任务列表
     */
    @PreAuthorize("@ss.hasPermi('pdi:task:export')")
    @Log(title = "报告生成任务", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, ReportTask reportTask)
    {
        List<ReportTask> list = reportTaskService.selectReportTaskList(reportTask);
        ExcelUtil<ReportTask> util = new ExcelUtil<ReportTask>(ReportTask.class);
        util.exportExcel(response, list, "报告生成任务数据");
    }

    /**
     * 获取报告生成任务详细信息
     */
    @PreAuthorize("@ss.hasPermi('pdi:task:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return success(reportTaskService.selectReportTaskById(id));
    }

    /**
     * 新增报告生成任务
     */
    @PreAuthorize("@ss.hasPermi('pdi:task:add')")
    @Log(title = "报告生成任务", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody ReportTask reportTask)
    {
        return toAjax(reportTaskService.insertReportTask(reportTask));
    }

    /**
     * 修改报告生成任务
     */
    @PreAuthorize("@ss.hasPermi('pdi:task:edit')")
    @Log(title = "报告生成任务", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody ReportTask reportTask)
    {
        return toAjax(reportTaskService.updateReportTask(reportTask));
    }

    /**
     * 删除报告生成任务
     */
    @PreAuthorize("@ss.hasPermi('pdi:task:remove')")
    @Log(title = "报告生成任务", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(reportTaskService.deleteReportTaskByIds(ids));
    }
}
