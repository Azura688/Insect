package com.ruoyi.insectdata.service.impl;

import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.insectdata.domain.Data;
import com.ruoyi.insectdata.mapper.DataMapper;
import com.ruoyi.insectdata.service.IDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 识别数据Service业务层处理
 *
 * @author zmh
 * @date 2021-08-29
 */
@Service
public class DataServiceImpl implements IDataService
{
    @Autowired
    private DataMapper dataMapper;

    /**
     * 查询识别数据
     *
     * @param dataId 识别数据主键
     * @return 识别数据
     */
    @Override
    public Data selectDataByDataId(Integer dataId)
    {
        return dataMapper.selectDataByDataId(dataId);
    }

    /**
     * 查询识别数据列表
     *
     * @param data 识别数据
     * @return 识别数据
     */
    @Override
    public List<Data> selectDataList(Data data)
    {
        return dataMapper.selectDataList(data);
    }


    /**
     * 查询识别数据id数组
     *
     * @param  originalPictures
     * @return 识别数据
     */
    @Override
    public Integer[] selectDataIDs(String[] originalPictures) {
        return dataMapper.selectDataIDByoriginalPictures(originalPictures);
    }


    /**
     * 新增识别数据
     *
     * @param data 识别数据
     * @return 结果
     */
    @Override
    public int insertData(Data data)
    {
        data.setCreateTime(DateUtils.getNowDate());
        return dataMapper.insertData(data);
    }

    /**
     * 批量增加数据图片
     *
     * @param originalPictures 数据图片列表
     * @param equipmentId 设备号
     * @param photoTime 拍摄时间
     * @param photoArea 拍摄地点
     * @return 结果
     */
    @Override
    public int batchData(String[] originalPictures, Integer equipmentId, Date photoTime, String photoArea,String bugtype) {
        List<Data> list = new ArrayList<Data>();
        for (String img : originalPictures){
            Data data = new Data();
            data.setEquipmentId(equipmentId);
            data.setPhotoTime(photoTime);
            data.setPhotoArea(photoArea);
            data.setCreateTime(DateUtils.getNowDate());
            data.setOriginalPicture(img);
            data.setBugtype(bugtype);
            list.add(data);
        }
        if (list.size() > 0) {
            return dataMapper.batchData(list);
        }else {
            return 0;
        }
    }

    /**
     * 修改识别数据
     *
     * @param data 识别数据
     * @return 结果
     */
    @Override
    public int updateData(Data data)
    {
        data.setUpdateTime(DateUtils.getNowDate());
        return dataMapper.updateData(data);
    }

    /**
     * 批量删除识别数据
     *
     * @param dataIds 需要删除的识别数据主键
     * @return 结果
     */
    @Override
    public int deleteDataByDataIds(Integer[] dataIds)
    {
        return dataMapper.deleteDataByDataIds(dataIds);
    }

    /**
     * 删除识别数据信息
     *
     * @param dataId 识别数据主键
     * @return 结果
     */
    @Override
    public int deleteDataByDataId(Integer dataId)
    {
        return dataMapper.deleteDataByDataId(dataId);
    }

    /**
     * 根据用户地区查询相同地区设备的识别数据
     *
     * @param address 用户地区
     * @return 结果
     */
    @Override
    public List<Data> selectDataByUserAddress(String address) {
        String[] add = address.split("-");
        String province = add[0];
        String city = add[1];
        String county = add[2];

        System.out.println(province +"-"+ city +"-"+ county);

        return dataMapper.selectDataByAddress(province,city,county);
    }

    /**
     * 根据用户地区和时间查询识别数据
     *
     * @param address 用户地区
     * @param photoTime 拍摄时间
     * @return 结果
     */
    @Override
    public List<Data> selectDataByUserAddressAndTime(String address, Date photoTime) {
        String[] add = address.split("-");
        String province = add[0];
        String city = add[1];
        String county = add[2];

        System.out.println(province +"-"+ city +"-"+ county +"-"+ photoTime);

        return dataMapper.selectDataByAddressAndTime(province,city,county,photoTime);
    }
}
