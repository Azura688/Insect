package com.ruoyi.insectdata.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 识别结果对象 identification
 * 
 * @author zmh
 * @date 2021-08-29
 */
public class Identification extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 数据ID */
    @Excel(name = "数据ID")
    private Integer dataId;

    /** 昆虫ID */
    @Excel(name = "昆虫ID")
    private Integer insectId;

    /** 数量 */
    @Excel(name = "数量")
    private Integer number;

    /** 标记框 */
    @Excel(name = "标记框")
    private String markerBox;

    private Insect insect;

    public Insect getInsect(){
        return insect;
    }

    public void setInsect(Insect insect) {
        this.insect = insect;
    }

    public void setDataId(Integer dataId)
    {
        this.dataId = dataId;
    }

    public Integer getDataId() 
    {
        return dataId;
    }
    public void setInsectId(Integer insectId) 
    {
        this.insectId = insectId;
    }

    public Integer getInsectId() 
    {
        return insectId;
    }
    public void setNumber(Integer number) 
    {
        this.number = number;
    }

    public Integer getNumber() 
    {
        return number;
    }
    public void setMarkerBox(String markerBox)
    {
        this.markerBox = markerBox;
    }

    public String getMarkerBox()
    {
        return markerBox;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("dataId", getDataId())
            .append("insectId", getInsectId())
            .append("number", getNumber())
            .append("markerBox", getMarkerBox())
            .toString();
    }
}
