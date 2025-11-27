package me.ooify.pdi.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import me.ooify.common.annotation.Excel;
import me.ooify.common.core.domain.BaseEntity;

/**
 * 道路基础信息对象 road
 * 
 * @author ooify
 * @date 2025-11-25
 */
public class Road extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 道路ID */
    private Long id;

    /** 道路名称 */
    @Excel(name = "道路名称")
    private String name;

    /** 道路地理信息 */
    @Excel(name = "道路地理信息")
    private String geoData;

    /** 状态 */
    @Excel(name = "状态")
    private Long status;

    public void setId(Long id) 
    {
        this.id = id;
    }

    public Long getId() 
    {
        return id;
    }

    public void setName(String name) 
    {
        this.name = name;
    }

    public String getName() 
    {
        return name;
    }

    public void setGeoData(String geoData) 
    {
        this.geoData = geoData;
    }

    public String getGeoData() 
    {
        return geoData;
    }

    public void setStatus(Long status) 
    {
        this.status = status;
    }

    public Long getStatus() 
    {
        return status;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("name", getName())
            .append("geoData", getGeoData())
            .append("status", getStatus())
            .append("remark", getRemark())
            .append("createBy", getCreateBy())
            .append("createTime", getCreateTime())
            .append("updateTime", getUpdateTime())
            .toString();
    }
}
