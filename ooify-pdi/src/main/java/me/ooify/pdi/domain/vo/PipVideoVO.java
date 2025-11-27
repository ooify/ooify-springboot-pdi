package me.ooify.pdi.domain.vo;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PipVideoVO {
    /**
     * 主键ID
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
     * 文件大小
     */
    private Long fileSize;

    /**
     * 文件类型
     */
    private String mimeType;

    /**
     * 视频时长
     */
    private BigDecimal duration;

    /**
     * 分辨率
     */
    private String resolution;

    /**
     * 管道信息
     */
    private String pipeInfo;

    /**
     * 上传状态
     */
    private Long uploadStatus;

    /**
     * 创建时间
     */
    private String createTime;

    /**
     * 请求参数
     */
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private Map<String, Object> params;

    public Map<String, Object> getParams()
    {
        if (params == null)
        {
            params = new HashMap<>();
        }
        return params;
    }

}
