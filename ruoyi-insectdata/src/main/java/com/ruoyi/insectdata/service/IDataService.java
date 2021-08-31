package com.ruoyi.insectdata.service;

import java.util.List;
import com.ruoyi.insectdata.domain.Data;

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
}
