package me.ooify.pdi.service;

import java.util.List;
import me.ooify.pdi.domain.Road;

/**
 * 道路基础信息Service接口
 * 
 * @author ooify
 * @date 2025-11-25
 */
public interface IRoadService 
{
    /**
     * 查询道路基础信息
     * 
     * @param id 道路基础信息主键
     * @return 道路基础信息
     */
    public Road selectRoadById(Long id);

    /**
     * 查询道路基础信息列表
     * 
     * @param road 道路基础信息
     * @return 道路基础信息集合
     */
    public List<Road> selectRoadList(Road road);

    /**
     * 新增道路基础信息
     * 
     * @param road 道路基础信息
     * @return 结果
     */
    public int insertRoad(Road road);

    /**
     * 修改道路基础信息
     * 
     * @param road 道路基础信息
     * @return 结果
     */
    public int updateRoad(Road road);

    /**
     * 批量删除道路基础信息
     * 
     * @param ids 需要删除的道路基础信息主键集合
     * @return 结果
     */
    public int deleteRoadByIds(Long[] ids);

    /**
     * 删除道路基础信息信息
     * 
     * @param id 道路基础信息主键
     * @return 结果
     */
    public int deleteRoadById(Long id);
}
