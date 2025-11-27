package me.ooify.pdi.service.impl;

import java.util.List;
import me.ooify.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import me.ooify.pdi.mapper.RoadMapper;
import me.ooify.pdi.domain.Road;
import me.ooify.pdi.service.IRoadService;

/**
 * 道路基础信息Service业务层处理
 * 
 * @author ooify
 * @date 2025-11-25
 */
@Service
public class RoadServiceImpl implements IRoadService 
{
    @Autowired
    private RoadMapper roadMapper;

    /**
     * 查询道路基础信息
     * 
     * @param id 道路基础信息主键
     * @return 道路基础信息
     */
    @Override
    public Road selectRoadById(Long id)
    {
        return roadMapper.selectRoadById(id);
    }

    /**
     * 查询道路基础信息列表
     * 
     * @param road 道路基础信息
     * @return 道路基础信息
     */
    @Override
    public List<Road> selectRoadList(Road road)
    {
        return roadMapper.selectRoadList(road);
    }

    /**
     * 新增道路基础信息
     * 
     * @param road 道路基础信息
     * @return 结果
     */
    @Override
    public int insertRoad(Road road)
    {
        road.setCreateTime(DateUtils.getNowDate());
        return roadMapper.insertRoad(road);
    }

    /**
     * 修改道路基础信息
     * 
     * @param road 道路基础信息
     * @return 结果
     */
    @Override
    public int updateRoad(Road road)
    {
        road.setUpdateTime(DateUtils.getNowDate());
        return roadMapper.updateRoad(road);
    }

    /**
     * 批量删除道路基础信息
     * 
     * @param ids 需要删除的道路基础信息主键
     * @return 结果
     */
    @Override
    public int deleteRoadByIds(Long[] ids)
    {
        return roadMapper.deleteRoadByIds(ids);
    }

    /**
     * 删除道路基础信息信息
     * 
     * @param id 道路基础信息主键
     * @return 结果
     */
    @Override
    public int deleteRoadById(Long id)
    {
        return roadMapper.deleteRoadById(id);
    }
}
