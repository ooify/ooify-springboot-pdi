package me.ooify.pdi.domain.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PipVideoTableVO {
    /**
     * 视频ID
     */
    private Long id;

    /**
     * 视频名
     */
    private String videoName;

    /**
     * 视频
     */
    private String videoUrl;

    /**
     * 道路ID
     */
    private Long roadId;

    /**
     * 缩略图
     */
    private String thumbnailUrl;

    /**
     * 管道信息
     */
    private String pipeInfo;

    /**
     * 上传状态
     */
    private Long uploadStatus;

    /**
     * 任务状态
     */
    private Long taskStatus;

    /**
     * 报告地址
     */
    private String reportUrl;

    /**
     * 缺陷数量
     */
    private Long defectCount;

    /**
     * 创建时间
     */
    private String createTime;

    /**
     * 创建者
     */
    private String createBy;


    /**
     * 请求参数
     */
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private Map<String, Object> params;

    public Map<String, Object> getParams() {
        if (params == null) {
            params = new HashMap<>();
        }
        return params;
    }

}
