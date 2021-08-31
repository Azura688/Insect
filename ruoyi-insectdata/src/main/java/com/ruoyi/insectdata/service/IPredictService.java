package com.ruoyi.insectdata.service;

import java.util.List;
import com.ruoyi.insectdata.domain.Predict;

/**
 * 预测结果Service接口
 * 
 * @author zmh
 * @date 2021-08-29
 */
public interface IPredictService 
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
     * 批量删除预测结果
     * 
     * @param insectIds 需要删除的预测结果主键集合
     * @return 结果
     */
    public int deletePredictByInsectIds(Integer[] insectIds);

    /**
     * 删除预测结果信息
     * 
     * @param insectId 预测结果主键
     * @return 结果
     */
    public int deletePredictByInsectId(Integer insectId);
}
