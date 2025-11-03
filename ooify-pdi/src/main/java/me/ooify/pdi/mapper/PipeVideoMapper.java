package me.ooify.pdi.mapper;

import java.util.List;
import me.ooify.pdi.domain.PipeVideo;

/**
 * 管道视频Mapper接口
 * 
 * @author ooify
 * @date 2025-11-03
 */
public interface PipeVideoMapper 
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
     * 删除管道视频
     * 
     * @param id 管道视频主键
     * @return 结果
     */
    public int deletePipeVideoById(Long id);

    /**
     * 批量删除管道视频
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deletePipeVideoByIds(Long[] ids);
}
