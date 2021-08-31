package com.ruoyi.insectdata.domain;

import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 预测结果对象 predict
 * 
 * @author zmh
 * @date 2021-08-29
 */
public class Predict extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 昆虫ID */
    private Integer insectId;

    /** 预测时间 */
    private Date predictTime;

    /** 预测结果 */
    @Excel(name = "预测结果")
    private Integer predictNum;

    public void setInsectId(Integer insectId) 
    {
        this.insectId = insectId;
    }

    public Integer getInsectId() 
    {
        return insectId;
    }
    public void setPredictTime(Date predictTime) 
    {
        this.predictTime = predictTime;
    }

    public Date getPredictTime() 
    {
        return predictTime;
    }
    public void setPredictNum(Integer predictNum) 
    {
        this.predictNum = predictNum;
    }

    public Integer getPredictNum() 
    {
        return predictNum;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("insectId", getInsectId())
            .append("predictTime", getPredictTime())
            .append("predictNum", getPredictNum())
            .append("createBy", getCreateBy())
            .append("createTime", getCreateTime())
            .append("updateBy", getUpdateBy())
            .append("updateTime", getUpdateTime())
            .toString();
    }
}
