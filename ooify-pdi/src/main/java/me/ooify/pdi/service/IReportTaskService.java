package me.ooify.pdi.service;

import java.util.List;
import me.ooify.pdi.domain.ReportTask;

/**
 * 报告生成任务Service接口
 * 
 * @author ooify
 * @date 2025-11-03
 */
public interface IReportTaskService 
{
    /**
     * 查询报告生成任务
     * 
     * @param id 报告生成任务主键
     * @return 报告生成任务
     */
    public ReportTask selectReportTaskById(Long id);

    /**
     * 查询报告生成任务列表
     * 
     * @param reportTask 报告生成任务
     * @return 报告生成任务集合
     */
    public List<ReportTask> selectReportTaskList(ReportTask reportTask);

    /**
     * 新增报告生成任务
     * 
     * @param reportTask 报告生成任务
     * @return 结果
     */
    public int insertReportTask(ReportTask reportTask);

    /**
     * 修改报告生成任务
     * 
     * @param reportTask 报告生成任务
     * @return 结果
     */
    public int updateReportTask(ReportTask reportTask);

    /**
     * 批量删除报告生成任务
     * 
     * @param ids 需要删除的报告生成任务主键集合
     * @return 结果
     */
    public int deleteReportTaskByIds(Long[] ids);

    /**
     * 删除报告生成任务信息
     * 
     * @param id 报告生成任务主键
     * @return 结果
     */
    public int deleteReportTaskById(Long id);
}
