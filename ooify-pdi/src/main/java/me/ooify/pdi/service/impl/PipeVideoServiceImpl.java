package me.ooify.pdi.service.impl;

import java.util.List;
import me.ooify.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import me.ooify.pdi.mapper.PipeVideoMapper;
import me.ooify.pdi.domain.PipeVideo;
import me.ooify.pdi.service.IPipeVideoService;

/**
 * 管道视频Service业务层处理
 * 
 * @author ooify
 * @date 2025-11-03
 */
@Service
public class PipeVideoServiceImpl implements IPipeVideoService 
{
    @Autowired
    private PipeVideoMapper pipeVideoMapper;

    /**
     * 查询管道视频
     * 
     * @param id 管道视频主键
     * @return 管道视频
     */
    @Override
    public PipeVideo selectPipeVideoById(Long id)
    {
        return pipeVideoMapper.selectPipeVideoById(id);
    }

    /**
     * 查询管道视频列表
     * 
     * @param pipeVideo 管道视频
     * @return 管道视频
     */
    @Override
    public List<PipeVideo> selectPipeVideoList(PipeVideo pipeVideo)
    {
        return pipeVideoMapper.selectPipeVideoList(pipeVideo);
    }

    /**
     * 新增管道视频
     * 
     * @param pipeVideo 管道视频
     * @return 结果
     */
    @Override
    public int insertPipeVideo(PipeVideo pipeVideo)
    {
        pipeVideo.setCreateTime(DateUtils.getNowDate());
        return pipeVideoMapper.insertPipeVideo(pipeVideo);
    }

    /**
     * 修改管道视频
     * 
     * @param pipeVideo 管道视频
     * @return 结果
     */
    @Override
    public int updatePipeVideo(PipeVideo pipeVideo)
    {
        pipeVideo.setUpdateTime(DateUtils.getNowDate());
        return pipeVideoMapper.updatePipeVideo(pipeVideo);
    }

    /**
     * 批量删除管道视频
     * 
     * @param ids 需要删除的管道视频主键
     * @return 结果
     */
    @Override
    public int deletePipeVideoByIds(Long[] ids)
    {
        return pipeVideoMapper.deletePipeVideoByIds(ids);
    }

    /**
     * 删除管道视频信息
     * 
     * @param id 管道视频主键
     * @return 结果
     */
    @Override
    public int deletePipeVideoById(Long id)
    {
        return pipeVideoMapper.deletePipeVideoById(id);
    }
}
