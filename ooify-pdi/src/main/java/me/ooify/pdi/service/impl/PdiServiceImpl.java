package me.ooify.pdi.service.impl;

import me.ooify.common.utils.SecurityUtils;
import me.ooify.pdi.domain.PipeVideo;
import me.ooify.pdi.domain.vo.PipVideoVO;
import me.ooify.pdi.mapper.PdiMapper;
import me.ooify.pdi.service.IPdiService;
import me.ooify.pdi.service.IPipeVideoService;
import me.ooify.pdi.service.tool.MessagingService;
import me.ooify.pdi.service.tool.OSSService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

@Service
public class PdiServiceImpl implements IPdiService {

    @Autowired
    private PdiMapper pdiMapper;

    @Autowired
    private IPipeVideoService pipeVideoService;

    @Autowired
    private MessagingService messagingService;

    @Autowired
    private OSSService ossService;

    @Override
    public List<PipVideoVO> selectPipeVideoVOListByUser(PipVideoVO pipVideoVO, String username) {

        return pdiMapper.selectPipeVideoVOListByUser(pipVideoVO, username);
    }

    @Override
    public PipVideoVO selectPipeVideoVOById(Long id) {
        return pdiMapper.selectPipeVideoVOById(id);
    }

    @Override
    public Map<String, String> handleUserUpload(PipVideoVO pipVideoVO, MultipartFile image) throws Exception {
        String thumbnail_url = ossService.uploadOSS(image, "thumbnail/" + SecurityUtils.getUsername());
        PipeVideo pipeVideo = new PipeVideo();
        BeanUtils.copyProperties(pipVideoVO, pipeVideo);
        pipeVideo.setCreateBy(SecurityUtils.getUsername());
        pipeVideo.setThumbnailUrl(thumbnail_url);
//        上传中
        pipeVideo.setUploadStatus(0L);
//        创建视频记录
        pipeVideoService.insertPipeVideo(pipeVideo);
//        发送 OCR 消息
        messagingService.sendOCRMessage(pipeVideo.getId(), thumbnail_url);
//        返回签名信息
        return ossService.getPostSignatureForOssUpload(pipeVideo.getId());
    }

}
