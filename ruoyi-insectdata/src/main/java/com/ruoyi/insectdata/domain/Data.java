package com.ruoyi.insectdata.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.util.Date;

/**
 * 数据对象 data
 *
 * @author zmh
 * @date 2021-11-22
 */
public class Data extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 数据ID */
    private Integer dataId;

    /** 原始图片 */
    @Excel(name = "原始图片")
    private String originalPicture;

    /** 标记框 */
    @Excel(name = "标记框")
    private String markerBox;

    /** 标记图片 */
    @Excel(name = "标记图片")
    private String tagPicture;

    /** 拍摄时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Excel(name = "拍摄时间", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss")//
    private Date photoTime;

    /** 拍摄地点 */
    @Excel(name = "拍摄地点")
    private String photoArea;

    /** 设备号 */
    @Excel(name = "设备号")
    private Integer equipmentId;

    /** 虫子种类 */
    @Excel(name = "虫子种类")
    private String bugtype;

    public String getBugtype() {
        return bugtype;
    }

    public void setBugtype(String bugtype) {
        this.bugtype = bugtype;
    }

    public void setDataId(Integer dataId)
    {
        this.dataId = dataId;
    }

    public Integer getDataId()
    {
        return dataId;
    }
    public void setOriginalPicture(String originalPicture)
    {
        this.originalPicture = originalPicture;
    }

    public String getOriginalPicture()
    {
        return originalPicture;
    }
    public void setMarkerBox(String markerBox)
    {
        this.markerBox = markerBox;
    }

    public String getMarkerBox()
    {
        return markerBox;
    }
    public void setTagPicture(String tagPicture)
    {
        this.tagPicture = tagPicture;
    }

    public String getTagPicture()
    {
        return tagPicture;
    }
    public void setPhotoTime(Date photoTime)
    {
        this.photoTime = photoTime;
    }

    public Date getPhotoTime()
    {
        return photoTime;
    }
    public void setPhotoArea(String photoArea)
    {
        this.photoArea = photoArea;
    }

    public String getPhotoArea()
    {
        return photoArea;
    }
    public void setEquipmentId(Integer equipmentId)
    {
        this.equipmentId = equipmentId;
    }

    public Integer getEquipmentId()
    {
        return equipmentId;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("dataId", getDataId())
            .append("originalPicture", getOriginalPicture())
            .append("markerBox", getMarkerBox())
            .append("tagPicture", getTagPicture())
            .append("photoTime", getPhotoTime())
            .append("photoArea", getPhotoArea())
            .append("equipmentId", getEquipmentId())
            .append("createBy", getCreateBy())
            .append("createTime", getCreateTime())
            .append("updateBy", getUpdateBy())
            .append("updateTime", getUpdateTime())
            .append("bugtype",getBugtype())
            .toString();
    }
}
