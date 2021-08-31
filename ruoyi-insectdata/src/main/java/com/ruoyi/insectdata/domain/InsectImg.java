package com.ruoyi.insectdata.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 昆虫图片对象 insect_img
 * 
 * @author zmh
 * @date 2021-08-29
 */
public class InsectImg extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 昆虫ID */
    private Long insectId;

    /** 昆虫图片 */
    private String img;

    public void setInsectId(Long insectId) 
    {
        this.insectId = insectId;
    }

    public Long getInsectId() 
    {
        return insectId;
    }
    public void setImg(String img) 
    {
        this.img = img;
    }

    public String getImg() 
    {
        return img;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("insectId", getInsectId())
            .append("img", getImg())
            .append("createBy", getCreateBy())
            .append("createTime", getCreateTime())
            .append("updateBy", getUpdateBy())
            .append("updateTime", getUpdateTime())
            .toString();
    }
}
