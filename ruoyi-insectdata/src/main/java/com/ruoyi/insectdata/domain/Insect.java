package com.ruoyi.insectdata.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.TreeEntity;

/**
 * 昆虫对象 insect
 * 
 * @author zmh
 * @date 2021-08-29
 */
public class Insect extends TreeEntity
{
    private static final long serialVersionUID = 1L;

    /** 昆虫ID */
    private Integer insectId;

    /** 昆虫名称 */
    @Excel(name = "昆虫名称")
    private String insectName;

    /** 类别： 0 目 1 科 2 种 */
    @Excel(name = "类别： 0 目 1 科 2 种")
    private String type;

    public void setInsectId(Integer insectId) 
    {
        this.insectId = insectId;
    }

    public Integer getInsectId() 
    {
        return insectId;
    }
    public void setInsectName(String insectName) 
    {
        this.insectName = insectName;
    }

    public String getInsectName() 
    {
        return insectName;
    }
    public void setType(String type) 
    {
        this.type = type;
    }

    public String getType() 
    {
        return type;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("insectId", getInsectId())
            .append("parentId", getParentId())
            .append("ancestors", getAncestors())
            .append("insectName", getInsectName())
            .append("orderNum", getOrderNum())
            .append("type", getType())
            .append("createBy", getCreateBy())
            .append("createTime", getCreateTime())
            .append("updateBy", getUpdateBy())
            .append("updateTime", getUpdateTime())
            .toString();
    }
}
