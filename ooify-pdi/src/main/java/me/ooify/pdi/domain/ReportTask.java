package me.ooify.pdi.domain;

import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import me.ooify.common.annotation.Excel;
import me.ooify.common.core.domain.BaseEntity;

/**
 * 报告生成任务对象 report_task
 * 
 * @author ooify
 * @date 2025-11-03
 */
public class ReportTask extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 主键ID */
    private Long id;

    /** 任务编号 */
    @Excel(name = "任务编号")
    private String taskNo;

    /** 视频ID */
    @Excel(name = "视频ID")
    private Long videoId;

    /** 视频 */
    @Excel(name = "视频")
    private String videoUrl;

    /** 管道信息 */
    @Excel(name = "管道信息")
    private String pipeInfo;

    /** 任务状态 */
    @Excel(name = "任务状态")
    private Long taskStatus;

    /** 队列位置 */
    private Long queuePosition;

    /** 重试次数 */
    private Long retryCount;

    /** 失败原因简述 */
    private String errorMessage;

    /** Flask任务ID(用于跨系统追踪) */
    private String flaskTaskId;

    /** 报告文档 */
    @Excel(name = "报告文档")
    private String reportUrl;

    /** OSSKey */
    private String ossObjectKey;

    /** 开始时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "开始时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date startTime;

    /** 完成时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "完成时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date endTime;

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

    public void setTaskNo(String taskNo) 
    {
        this.taskNo = taskNo;
    }

    public String getTaskNo() 
    {
        return taskNo;
    }

    public void setVideoId(Long videoId) 
    {
        this.videoId = videoId;
    }

    public Long getVideoId() 
    {
        return videoId;
    }

    public void setVideoUrl(String videoUrl) 
    {
        this.videoUrl = videoUrl;
    }

    public String getVideoUrl() 
    {
        return videoUrl;
    }

    public void setPipeInfo(String pipeInfo) 
    {
        this.pipeInfo = pipeInfo;
    }

    public String getPipeInfo() 
    {
        return pipeInfo;
    }

    public void setTaskStatus(Long taskStatus) 
    {
        this.taskStatus = taskStatus;
    }

    public Long getTaskStatus() 
    {
        return taskStatus;
    }

    public void setQueuePosition(Long queuePosition) 
    {
        this.queuePosition = queuePosition;
    }

    public Long getQueuePosition() 
    {
        return queuePosition;
    }

    public void setRetryCount(Long retryCount) 
    {
        this.retryCount = retryCount;
    }

    public Long getRetryCount() 
    {
        return retryCount;
    }

    public void setErrorMessage(String errorMessage) 
    {
        this.errorMessage = errorMessage;
    }

    public String getErrorMessage() 
    {
        return errorMessage;
    }

    public void setFlaskTaskId(String flaskTaskId) 
    {
        this.flaskTaskId = flaskTaskId;
    }

    public String getFlaskTaskId() 
    {
        return flaskTaskId;
    }

    public void setReportUrl(String reportUrl) 
    {
        this.reportUrl = reportUrl;
    }

    public String getReportUrl() 
    {
        return reportUrl;
    }

    public void setOssObjectKey(String ossObjectKey) 
    {
        this.ossObjectKey = ossObjectKey;
    }

    public String getOssObjectKey() 
    {
        return ossObjectKey;
    }

    public void setStartTime(Date startTime) 
    {
        this.startTime = startTime;
    }

    public Date getStartTime() 
    {
        return startTime;
    }

    public void setEndTime(Date endTime) 
    {
        this.endTime = endTime;
    }

    public Date getEndTime() 
    {
        return endTime;
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
            .append("taskNo", getTaskNo())
            .append("videoId", getVideoId())
            .append("videoUrl", getVideoUrl())
            .append("pipeInfo", getPipeInfo())
            .append("taskStatus", getTaskStatus())
            .append("queuePosition", getQueuePosition())
            .append("retryCount", getRetryCount())
            .append("errorMessage", getErrorMessage())
            .append("flaskTaskId", getFlaskTaskId())
            .append("reportUrl", getReportUrl())
            .append("ossObjectKey", getOssObjectKey())
            .append("startTime", getStartTime())
            .append("endTime", getEndTime())
            .append("createBy", getCreateBy())
            .append("isDeleted", getIsDeleted())
            .append("remark", getRemark())
            .append("createTime", getCreateTime())
            .append("updateTime", getUpdateTime())
            .toString();
    }
}
