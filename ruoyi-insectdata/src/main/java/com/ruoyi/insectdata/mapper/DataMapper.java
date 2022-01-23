package com.ruoyi.insectdata.mapper;

import java.util.Date;
import java.util.List;
import com.ruoyi.insectdata.domain.Data;
import org.apache.ibatis.annotations.Param;
import org.springframework.security.core.parameters.P;

/**
 * 识别数据Mapper接口
 * 
 * @author zmh
 * @date 2021-08-29
 */
public interface DataMapper 
{
    /**
     * 查询识别数据
     * 
     * @param dataId 识别数据主键
     * @return 识别数据
     */
    public Data selectDataByDataId(Integer dataId);

    /**
     * 查询识别数据列表
     * 
     * @param data 识别数据
     * @return 识别数据集合
     */
    public List<Data> selectDataList(Data data);

    /**
     * 新增识别数据
     * 
     * @param data 识别数据
     * @return 结果
     */
    public int insertData(Data data);

    /**
     * 修改识别数据
     * 
     * @param data 识别数据
     * @return 结果
     */
    public int updateData(Data data);

    /**
     * 删除识别数据
     * 
     * @param dataId 识别数据主键
     * @return 结果
     */
    public int deleteDataByDataId(Integer dataId);

    /**
     * 批量删除识别数据
     * 
     * @param dataIds 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteDataByDataIds(Integer[] dataIds);

    /**
     * 根据用户地区查询相同地区设备的识别数据
     *
     * @param province 用户地址-省
     * @param city 用户地址-市
     * @param county 用户地址-区
     * @return 结果
     */
    public List<Data> selectDataByAddress(@Param("province")String province, @Param("city")String city, @Param("county")String county);

    /**
     * 根据用户地区查询相同地区设备的识别数据
     *
     * @param province 用户地址-省
     * @param city 用户地址-市
     * @param county 用户地址-区
     * @param photoTime 拍摄时间
     * @return 结果
     */
    public List<Data> selectDataByAddressAndTime(@Param("province")String province, @Param("city")String city, @Param("county")String county, @Param("photoTime")Date photoTime);


}
