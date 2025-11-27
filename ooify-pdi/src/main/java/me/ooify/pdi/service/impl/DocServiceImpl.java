package me.ooify.pdi.service.impl;

import me.ooify.common.utils.SecurityUtils;
import me.ooify.pdi.domain.PipeVideo;
import me.ooify.pdi.domain.ReportTask;
import me.ooify.pdi.domain.vo.ReportTaskVO;
import me.ooify.pdi.mapper.DocMapper;
import me.ooify.pdi.service.IDocService;
import me.ooify.pdi.service.IPipeVideoService;
import me.ooify.pdi.service.IReportTaskService;
import me.ooify.pdi.service.tool.MessagingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DocServiceImpl implements IDocService {

    @Autowired
    private DocMapper docMapper;

    @Autowired
    private IPipeVideoService pipeVideoService;

    @Autowired
    private IReportTaskService reportTaskService;

    @Autowired
    private MessagingService messagingService;

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

    @Override
    public void generateReportByIds(Long[] ids) {
        for (Long id : ids) {
            PipeVideo pipeVideo = pipeVideoService.selectPipeVideoById(id);
            ReportTask reportTask = new ReportTask();
//            202510230001
            reportTask.setTaskNo("Task-"+System.currentTimeMillis());
            reportTask.setVideoId(pipeVideo.getId());
            reportTask.setVideoUrl(pipeVideo.getVideoUrl());
            reportTask.setPipeInfo(pipeVideo.getPipeInfo());
            
//            排队中
            reportTask.setTaskStatus(0L);
            reportTask.setCreateBy(SecurityUtils.getUsername());
            reportTaskService.insertReportTask(reportTask);

            messagingService.sendDocMessage(reportTask);
        }

    }
}
