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

    /**
     * 查询管道视频列表
     *
     * @param pipVideoVO 管道视频信息
     * @param username   用户名
     * @return 管道视频集合
     */
    @Override
    public List<PipVideoVO> selectPipeVideoVOListByUser(PipVideoVO pipVideoVO, String username) {

        return pdiMapper.selectPipeVideoVOListByUser(pipVideoVO, username);
    }

    /**
     * 查询管道视频详细信息
     *
     * @param id 管道视频主键
     * @return 管道视频详细信息
     */
    @Override
    public PipVideoVO selectPipeVideoVOById(Long id) {
        return pdiMapper.selectPipeVideoVOById(id);
    }

    /**
     * 处理用户上传请求，返回OSS上传签名信息
     *
     * @param pipVideoVO 管道视频信息
     * @return OSS上传签名信息
     */

    @Override
    public Map<String, String> handleUserUpload(PipVideoVO pipVideoVO) throws Exception {
        PipeVideo pipeVideo = new PipeVideo();
        BeanUtils.copyProperties(pipVideoVO, pipeVideo);
        pipeVideo.setCreateBy(SecurityUtils.getUsername());
//        设置状态为上传中
        pipeVideo.setUploadStatus(0L);
//        创建视频记录
        pipeVideoService.insertPipeVideo(pipeVideo);
//        返回签名信息
        return ossService.getPostSignatureForOssUpload(pipeVideo.getId());
    }

    /**
     * 处理上传回调
     *
     * @param pipVideoId  管道视频ID
     * @param pipVideoUrl 视频URL
     * @return void
     */

    @Override
    public void handleUploadCallback(Long pipVideoId, String pipVideoUrl,Long userId) {
        PipeVideo pipeVideo = new PipeVideo();
        pipeVideo.setId(pipVideoId);
        pipeVideo.setVideoUrl(pipVideoUrl);
        pipeVideo.setThumbnailUrl(pipVideoUrl + "?x-oss-process=video/snapshot,t_0,f_jpg,w_0,h_0,m_fast,ar_auto");
        // 信息待确定
        pipeVideo.setUploadStatus(3L);
        pipeVideoService.updatePipeVideo(pipeVideo);
        // 发送 OCR 消息
        messagingService.sendOCRMessage(pipeVideo.getId(), pipeVideo.getThumbnailUrl(),userId);
    }

    /**
     * 批量删除管道视频
     *
     * @param ids 需要删除的管道视频主键
     * @return 结果
     */
    @Override
    public int deletePipeVideoByIds(Long[] ids) {
        return pdiMapper.deletePipeVideoByIds(ids);
    }

    /**
     * 删除管道视频信息
     *
     * @param id 管道视频主键
     * @return 结果
     */
    @Override
    public int deletePipeVideoById(Long id) {
        return pdiMapper.deletePipeVideoById(id);
    }
}
