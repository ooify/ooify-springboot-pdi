package me.ooify.pdi.service.impl;

import me.ooify.pdi.domain.vo.ReportTaskVO;
import me.ooify.pdi.mapper.DocMapper;
import me.ooify.pdi.service.IDocService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DocServiceImpl implements IDocService {

    @Autowired
    private DocMapper docMapper;


    @Override
    public List<ReportTaskVO> selectReportTaskVOListByUser(ReportTaskVO reportTaskVO, String username) {
        return docMapper.selectReportTaskVOListByUser(reportTaskVO, username);
    }

    @Override
    public ReportTaskVO selectReportTaskVOById(Long id) {
        return docMapper.selectReportTaskVOById(id);
    }

    @Override
    public int deleteReportTaskByIds(Long[] ids) {
        return docMapper.deleteReportTaskByIds(ids);
    }

    @Override
    public int deleteReportTaskById(Long id) {
        return docMapper.deleteReportTaskById(id);
    }
}
