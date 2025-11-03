package me.ooify.pdi.service.impl;

import java.util.List;
import me.ooify.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import me.ooify.pdi.mapper.ReportTaskMapper;
import me.ooify.pdi.domain.ReportTask;
import me.ooify.pdi.service.IReportTaskService;

/**
 * 报告生成任务Service业务层处理
 * 
 * @author ooify
 * @date 2025-11-03
 */
@Service
public class ReportTaskServiceImpl implements IReportTaskService 
{
    @Autowired
    private ReportTaskMapper reportTaskMapper;

    /**
     * 查询报告生成任务
     * 
     * @param id 报告生成任务主键
     * @return 报告生成任务
     */
    @Override
    public ReportTask selectReportTaskById(Long id)
    {
        return reportTaskMapper.selectReportTaskById(id);
    }

    /**
     * 查询报告生成任务列表
     * 
     * @param reportTask 报告生成任务
     * @return 报告生成任务
     */
    @Override
    public List<ReportTask> selectReportTaskList(ReportTask reportTask)
    {
        return reportTaskMapper.selectReportTaskList(reportTask);
    }

    /**
     * 新增报告生成任务
     * 
     * @param reportTask 报告生成任务
     * @return 结果
     */
    @Override
    public int insertReportTask(ReportTask reportTask)
    {
        reportTask.setCreateTime(DateUtils.getNowDate());
        return reportTaskMapper.insertReportTask(reportTask);
    }

    /**
     * 修改报告生成任务
     * 
     * @param reportTask 报告生成任务
     * @return 结果
     */
    @Override
    public int updateReportTask(ReportTask reportTask)
    {
        reportTask.setUpdateTime(DateUtils.getNowDate());
        return reportTaskMapper.updateReportTask(reportTask);
    }

    /**
     * 批量删除报告生成任务
     * 
     * @param ids 需要删除的报告生成任务主键
     * @return 结果
     */
    @Override
    public int deleteReportTaskByIds(Long[] ids)
    {
        return reportTaskMapper.deleteReportTaskByIds(ids);
    }

    /**
     * 删除报告生成任务信息
     * 
     * @param id 报告生成任务主键
     * @return 结果
     */
    @Override
    public int deleteReportTaskById(Long id)
    {
        return reportTaskMapper.deleteReportTaskById(id);
    }
}
