package me.ooify.pdi.service;

import me.ooify.pdi.domain.PipeVideo;
import me.ooify.pdi.domain.vo.PipVideoVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * PdiService接口
 *
 * @author ooify
 * @date 2025-11-04
 */
public interface IPdiService {
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
}
