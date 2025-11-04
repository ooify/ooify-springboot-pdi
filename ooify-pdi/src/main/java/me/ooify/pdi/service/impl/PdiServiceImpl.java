package me.ooify.pdi.service.impl;

import me.ooify.pdi.domain.vo.PipVideoVO;
import me.ooify.pdi.mapper.PdiMapper;
import me.ooify.pdi.service.IPdiService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PdiServiceImpl implements IPdiService {

    @Autowired
    private PdiMapper pdiMapper;

    @Override
    public List<PipVideoVO> selectPipeVideoVOListByUser(PipVideoVO pipVideoVO, String username) {

        return pdiMapper.selectPipeVideoVOListByUser(pipVideoVO, username);
    }

    @Override
    public PipVideoVO selectPipeVideoVOById(Long id) {
        return pdiMapper.selectPipeVideoVOById(id);
    }
}
