package me.ooify.pdi.domain;

import java.math.BigDecimal;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import me.ooify.common.annotation.Excel;
import me.ooify.common.core.domain.BaseEntity;

/**
 * 管道视频对象 pipe_video
 * 
 * @author ooify
 * @date 2025-11-03
 */
public class PipeVideo extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 主键ID */
    private Long id;

    /** 视频名 */
    @Excel(name = "视频名")
    private String videoName;

    /** 视频 */
    @Excel(name = "视频")
    private String videoUrl;

    /** 缩略图 */
    @Excel(name = "缩略图")
    private String thumbnailUrl;

    /** 文件大小 */
    @Excel(name = "文件大小")
    private Long fileSize;

    /** 文件类型 */
    @Excel(name = "文件类型")
    private String mimeType;

    /** 视频时长 */
    @Excel(name = "视频时长")
    private BigDecimal duration;

    /** 分辨率 */
    @Excel(name = "分辨率")
    private String resolution;

    /** 管道信息 */
    @Excel(name = "管道信息")
    private String pipeInfo;

    /** 上传状态 */
    @Excel(name = "上传状态")
    private Long uploadStatus;

    /** 上传失败原因或错误信息 */
    private String uploadError;

    /** 是否删除 */
    @Excel(name = "是否删除")
    private Long isDeleted;

    public void setId(Long id) 
    {
        this.id = id;
    }

    public Long getId() 
    {
        return id;
    }

    public void setVideoName(String videoName) 
    {
        this.videoName = videoName;
    }

    public String getVideoName() 
    {
        return videoName;
    }

    public void setVideoUrl(String videoUrl) 
    {
        this.videoUrl = videoUrl;
    }

    public String getVideoUrl() 
    {
        return videoUrl;
    }

    public void setThumbnailUrl(String thumbnailUrl) 
    {
        this.thumbnailUrl = thumbnailUrl;
    }

    public String getThumbnailUrl() 
    {
        return thumbnailUrl;
    }

    public void setFileSize(Long fileSize) 
    {
        this.fileSize = fileSize;
    }

    public Long getFileSize() 
    {
        return fileSize;
    }

    public void setMimeType(String mimeType) 
    {
        this.mimeType = mimeType;
    }

    public String getMimeType() 
    {
        return mimeType;
    }

    public void setDuration(BigDecimal duration) 
    {
        this.duration = duration;
    }

    public BigDecimal getDuration() 
    {
        return duration;
    }

    public void setResolution(String resolution) 
    {
        this.resolution = resolution;
    }

    public String getResolution() 
    {
        return resolution;
    }

    public void setPipeInfo(String pipeInfo) 
    {
        this.pipeInfo = pipeInfo;
    }

    public String getPipeInfo() 
    {
        return pipeInfo;
    }

    public void setUploadStatus(Long uploadStatus) 
    {
        this.uploadStatus = uploadStatus;
    }

    public Long getUploadStatus() 
    {
        return uploadStatus;
    }

    public void setUploadError(String uploadError) 
    {
        this.uploadError = uploadError;
    }

    public String getUploadError() 
    {
        return uploadError;
    }

    public void setIsDeleted(Long isDeleted) 
    {
        this.isDeleted = isDeleted;
    }

    public Long getIsDeleted() 
    {
        return isDeleted;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("videoName", getVideoName())
            .append("videoUrl", getVideoUrl())
            .append("thumbnailUrl", getThumbnailUrl())
            .append("fileSize", getFileSize())
            .append("mimeType", getMimeType())
            .append("duration", getDuration())
            .append("resolution", getResolution())
            .append("pipeInfo", getPipeInfo())
            .append("uploadStatus", getUploadStatus())
            .append("uploadError", getUploadError())
            .append("createBy", getCreateBy())
            .append("isDeleted", getIsDeleted())
            .append("remark", getRemark())
            .append("createTime", getCreateTime())
            .append("updateTime", getUpdateTime())
            .toString();
    }
}
