package com.ruoyi.insectdata.service;

import com.ruoyi.insectdata.domain.Data;

import java.util.Date;
import java.util.List;

/**
 * 识别数据Service接口
 *
 * @author zmh
 * @date 2021-08-29
 */
public interface IDataService
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
     * 查询识别数据id列表
     *
     * @param originalPictures
     * @return 识别数据集合
     */
    public Integer[] selectDataIDs(String[] originalPictures);
    /**
     * 新增识别数据
     *
     * @param data 识别数据
     * @return 结果
     */
    public int insertData(Data data);

    /**
     * 批量增加数据图片
     *
     * @param originalPictures 数据图片列表
     * @param equipmentId 设备号
     * @param photoTime 拍摄时间
     * @param photoArea 拍摄地点
     * @return 结果
     */
    public int batchData(String[] originalPictures, Integer equipmentId, Date photoTime, String photoArea,String bugtype);

    /**
     * 修改识别数据
     *
     * @param data 识别数据
     * @return 结果
     */
    public int updateData(Data data);

    /**
     * 批量删除识别数据
     *
     * @param dataIds 需要删除的识别数据主键集合
     * @return 结果
     */
    public int deleteDataByDataIds(Integer[] dataIds);

    /**
     * 删除识别数据信息
     *
     * @param dataId 识别数据主键
     * @return 结果
     */
    public int deleteDataByDataId(Integer dataId);

    /**
     * 根据用户地区查询相同地区设备的识别数据
     *
     * @param address 用户地区
     * @return 结果
     */
    public List<Data> selectDataByUserAddress(String address);

    /**
     * 根据用户地区和时间查询识别数据
     *
     * @param address 用户地区
     * @param photoTime 拍摄时间
     * @return 结果
     */
    public List<Data> selectDataByUserAddressAndTime(String address, Date photoTime);
}
