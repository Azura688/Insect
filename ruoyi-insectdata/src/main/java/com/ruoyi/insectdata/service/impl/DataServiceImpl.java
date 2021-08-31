package com.ruoyi.insectdata.service.impl;

import java.util.List;
import com.ruoyi.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.insectdata.mapper.DataMapper;
import com.ruoyi.insectdata.domain.Data;
import com.ruoyi.insectdata.service.IDataService;

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
}
