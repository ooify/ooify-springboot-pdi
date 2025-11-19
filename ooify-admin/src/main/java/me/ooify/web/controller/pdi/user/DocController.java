package me.ooify.web.controller.pdi.user;

import me.ooify.common.core.controller.BaseController;
import me.ooify.common.core.domain.AjaxResult;
import me.ooify.common.core.page.TableDataInfo;
import me.ooify.common.utils.SecurityUtils;
import me.ooify.pdi.domain.PipeVideo;
import me.ooify.pdi.domain.ReportTask;
import me.ooify.pdi.domain.vo.PipVideoVO;
import me.ooify.pdi.domain.vo.ReportTaskVO;
import me.ooify.pdi.service.IDocService;
import me.ooify.pdi.service.IPipeVideoService;
import me.ooify.pdi.service.IReportTaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;


/**
 * 用户报告生成Controller
 *
 * @author ooify
 * @date 2025-11-18
 */
@RestController
@RequestMapping("/user/pdi/doc")
public class DocController extends BaseController {

    @Autowired
    private IDocService docService;

    @Autowired
    private IReportTaskService reportTaskService;

    @Autowired
    private IPipeVideoService pipeVideoService;

    /**
     * 查询用户报告列表
     */
    @PreAuthorize("@ss.hasRole('user')")
    @GetMapping("/list")
    public TableDataInfo list(ReportTaskVO reportTaskVO) {
        startPage();
        List<ReportTaskVO> list = docService.selectReportTaskVOListByUser(reportTaskVO, SecurityUtils.getUsername());
        return getDataTable(list);
    }


    /**
     * 获取用户报告详细信息
     */
    @PreAuthorize("@ss.hasRole('user')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id) {
        ReportTask reportTask = reportTaskService.selectReportTaskById(id);
        if (!SecurityUtils.getUsername().equals(reportTask.getCreateBy())) {
            return AjaxResult.error("没有权限访问该报告");
        }
        ReportTaskVO reportTaskVO = docService.selectReportTaskVOById(id);
        return success(reportTaskVO);
    }

    /**
     * 生成视频报告
     */
    @PreAuthorize("@ss.hasRole('user')")
    @PostMapping("/generate/{ids}")
    public AjaxResult generateReport(@PathVariable Long[] ids) {
        docService.generateReportByIds(ids);
        return AjaxResult.success("报告生成任务已提交");

    }

    /**
     * 删除用户报告
     */
    @PreAuthorize("@ss.hasRole('user')")
    @DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids) {
        return toAjax(docService.deleteReportTaskByIds(ids));
    }


}
