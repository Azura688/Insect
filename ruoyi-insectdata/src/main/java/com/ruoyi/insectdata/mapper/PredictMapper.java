package com.ruoyi.insectdata.mapper;

import java.util.List;
import com.ruoyi.insectdata.domain.Predict;

/**
 * 预测结果Mapper接口
 * 
 * @author zmh
 * @date 2021-08-29
 */
public interface PredictMapper 
{
    /**
     * 查询预测结果
     * 
     * @param insectId 预测结果主键
     * @return 预测结果
     */
    public Predict selectPredictByInsectId(Integer insectId);

    /**
     * 查询预测结果列表
     * 
     * @param predict 预测结果
     * @return 预测结果集合
     */
    public List<Predict> selectPredictList(Predict predict);

    /**
     * 新增预测结果
     * 
     * @param predict 预测结果
     * @return 结果
     */
    public int insertPredict(Predict predict);

    /**
     * 修改预测结果
     * 
     * @param predict 预测结果
     * @return 结果
     */
    public int updatePredict(Predict predict);

    /**
     * 删除预测结果
     * 
     * @param insectId 预测结果主键
     * @return 结果
     */
    public int deletePredictByInsectId(Integer insectId);

    /**
     * 批量删除预测结果
     * 
     * @param insectIds 需要删除的数据主键集合
     * @return 结果
     */
    public int deletePredictByInsectIds(Integer[] insectIds);
}
