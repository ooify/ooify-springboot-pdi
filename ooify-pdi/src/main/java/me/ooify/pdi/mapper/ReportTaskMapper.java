package me.ooify.pdi.mapper;

import java.util.List;
import me.ooify.pdi.domain.ReportTask;

/**
 * 报告生成任务Mapper接口
 * 
 * @author ooify
 * @date 2025-11-03
 */
public interface ReportTaskMapper 
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
     * 删除报告生成任务
     * 
     * @param id 报告生成任务主键
     * @return 结果
     */
    public int deleteReportTaskById(Long id);

    /**
     * 批量删除报告生成任务
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteReportTaskByIds(Long[] ids);
}
