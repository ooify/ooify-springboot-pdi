package me.ooify.pdi.service;

import me.ooify.pdi.domain.vo.ReportTaskVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * DocService接口
 *
 * @author ooify
 * @date 2025-11-18
 */
public interface IDocService {

    /**
     * 查询用户文档列表
     *
     * @param reportTaskVO 报告任务VO
     * @param username     用户名
     * @return 报告任务集合
     */
    public List<ReportTaskVO> selectReportTaskVOListByUser(@Param("reportTaskVO") ReportTaskVO reportTaskVO, @Param("username") String username);

    /**
     * 根据ID查询报告任务VO
     *
     * @param id 报告任务主键
     * @return 报告任务VO
     */
    public ReportTaskVO selectReportTaskVOById(Long id);

    /**
     * 删除报告任务
     *
     * @param ids 报告任务主键数组
     * @return 结果
     */
    public int deleteReportTaskByIds(Long[] ids);

    /**
     * 删除报告任务
     *
     * @param id 报告任务主键
     * @return 结果
     */
    public int deleteReportTaskById(Long id);


    /**
     * 生成报告任务
     *
     * @param ids 管道视频主键数组
     */
    public void generateReportByIds(Long[] ids);

}
