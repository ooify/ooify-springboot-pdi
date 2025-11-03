package me.ooify.pdi.service;

import java.util.List;
import me.ooify.pdi.domain.PipeVideo;

/**
 * 管道视频Service接口
 * 
 * @author ooify
 * @date 2025-11-03
 */
public interface IPipeVideoService 
{
    /**
     * 查询管道视频
     * 
     * @param id 管道视频主键
     * @return 管道视频
     */
    public PipeVideo selectPipeVideoById(Long id);

    /**
     * 查询管道视频列表
     * 
     * @param pipeVideo 管道视频
     * @return 管道视频集合
     */
    public List<PipeVideo> selectPipeVideoList(PipeVideo pipeVideo);

    /**
     * 新增管道视频
     * 
     * @param pipeVideo 管道视频
     * @return 结果
     */
    public int insertPipeVideo(PipeVideo pipeVideo);

    /**
     * 修改管道视频
     * 
     * @param pipeVideo 管道视频
     * @return 结果
     */
    public int updatePipeVideo(PipeVideo pipeVideo);

    /**
     * 批量删除管道视频
     * 
     * @param ids 需要删除的管道视频主键集合
     * @return 结果
     */
    public int deletePipeVideoByIds(Long[] ids);

    /**
     * 删除管道视频信息
     * 
     * @param id 管道视频主键
     * @return 结果
     */
    public int deletePipeVideoById(Long id);
}
