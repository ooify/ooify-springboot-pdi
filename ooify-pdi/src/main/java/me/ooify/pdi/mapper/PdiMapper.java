package me.ooify.pdi.mapper;

import me.ooify.pdi.domain.vo.PipVideoVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * PdiMapper接口
 *
 * @author ooify
 * @date 2025-11-04
 */
@Mapper
public interface PdiMapper {
    /**
     * 查询用户视频列表
     *
     * @param pipVideoVO 管道视频VO
     * @return 管道视频集合
     */
    public List<PipVideoVO> selectPipeVideoVOListByUser(@Param("pipVideoVO") PipVideoVO pipVideoVO, @Param("username") String username);

    /**
     * 根据ID查询管道视频VO
     *
     * @param id 管道视频主键
     * @return 管道视频VO
     */
    public PipVideoVO selectPipeVideoVOById(Long id);

    /**
     * 删除管道视频,源文件不删除
     *
     * @param id 管道视频主键
     * @return 结果
     */
    public int deletePipeVideoById(Long id);

    /**
     * 批量删除管道视频,源文件不删除
     *
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deletePipeVideoByIds(Long[] ids);
}
