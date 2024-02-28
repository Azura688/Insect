package com.ruoyi.insectdata.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 设备对象 equipment
 *
 * @author zmh
 * @date 2021-10-21
 */
public class Equipment extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 设备ID */
    private Integer id;

    /** 经度 */
    @Excel(name = "经度")
    private String longitude;

    /** 纬度 */
    @Excel(name = "纬度")
    private String latitude;

    /** 工作状态 1 正常 2 暂停 */
    @Excel(name = "工作状态 1 正常 2 暂停")
    private String workStatus;

    /** 挡雨板状态 1 正常 2 暂停 */
    @Excel(name = "挡雨板状态 1 正常 2 暂停")
    private String weathershieldStatus;

    /** 电量 */
    @Excel(name = "电量")
    private String electricity;

    /** 所在地气象 */
    @Excel(name = "所在地气象")
    private String weather;

    /** 省 */
    @Excel(name = "省")
    private String province;

    /** 市 */
    @Excel(name = "市")
    private String city;

    /** 县 */
    @Excel(name = "县")
    private String county;

    /** 详细地址 */
    @Excel(name = "详细地址")
    private String detail;

    /** 虫子种类 */
    @Excel(name = "虫子种类")
    private String bugtype;

    public String getBugtype() {
        return bugtype;
    }

    public void setBugtype(String bugtype) {
        this.bugtype = bugtype;
    }

    public void setId(Integer id)
    {
        this.id = id;
    }

    public Integer getId()
    {
        return id;
    }
    public void setLongitude(String longitude)
    {
        this.longitude = longitude;
    }

    public String getLongitude()
    {
        return longitude;
    }
    public void setLatitude(String latitude)
    {
        this.latitude = latitude;
    }

    public String getLatitude()
    {
        return latitude;
    }
    public void setWorkStatus(String workStatus)
    {
        this.workStatus = workStatus;
    }

    public String getWorkStatus()
    {
        return workStatus;
    }
    public void setWeathershieldStatus(String weathershieldStatus)
    {
        this.weathershieldStatus = weathershieldStatus;
    }

    public String getWeathershieldStatus()
    {
        return weathershieldStatus;
    }
    public void setElectricity(String electricity)
    {
        this.electricity = electricity;
    }

    public String getElectricity()
    {
        return electricity;
    }
    public void setWeather(String weather)
    {
        this.weather = weather;
    }

    public String getWeather()
    {
        return weather;
    }
    public void setProvince(String province)
    {
        this.province = province;
    }

    public String getProvince()
    {
        return province;
    }
    public void setCity(String city)
    {
        this.city = city;
    }

    public String getCity()
    {
        return city;
    }
    public void setCounty(String county)
    {
        this.county = county;
    }

    public String getCounty()
    {
        return county;
    }
    public void setDetail(String detail)
    {
        this.detail = detail;
    }

    public String getDetail()
    {
        return detail;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("longitude", getLongitude())
            .append("latitude", getLatitude())
            .append("workStatus", getWorkStatus())
            .append("weathershieldStatus", getWeathershieldStatus())
            .append("electricity", getElectricity())
            .append("weather", getWeather())
            .append("createBy", getCreateBy())
            .append("createTime", getCreateTime())
            .append("updateBy", getUpdateBy())
            .append("updateTime", getUpdateTime())
            .append("province", getProvince())
            .append("city", getCity())
            .append("county", getCounty())
            .append("detail", getDetail())
            .append("bugtype",getBugtype())
            .toString();
    }
}
