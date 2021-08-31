package com.ruoyi.insectdata.service.impl;

import java.util.List;
import com.ruoyi.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.insectdata.mapper.PredictMapper;
import com.ruoyi.insectdata.domain.Predict;
import com.ruoyi.insectdata.service.IPredictService;

/**
 * 预测结果Service业务层处理
 * 
 * @author zmh
 * @date 2021-08-29
 */
@Service
public class PredictServiceImpl implements IPredictService 
{
    @Autowired
    private PredictMapper predictMapper;

    /**
     * 查询预测结果
     * 
     * @param insectId 预测结果主键
     * @return 预测结果
     */
    @Override
    public Predict selectPredictByInsectId(Integer insectId)
    {
        return predictMapper.selectPredictByInsectId(insectId);
    }

    /**
     * 查询预测结果列表
     * 
     * @param predict 预测结果
     * @return 预测结果
     */
    @Override
    public List<Predict> selectPredictList(Predict predict)
    {
        return predictMapper.selectPredictList(predict);
    }

    /**
     * 新增预测结果
     * 
     * @param predict 预测结果
     * @return 结果
     */
    @Override
    public int insertPredict(Predict predict)
    {
        predict.setCreateTime(DateUtils.getNowDate());
        return predictMapper.insertPredict(predict);
    }

    /**
     * 修改预测结果
     * 
     * @param predict 预测结果
     * @return 结果
     */
    @Override
    public int updatePredict(Predict predict)
    {
        predict.setUpdateTime(DateUtils.getNowDate());
        return predictMapper.updatePredict(predict);
    }

    /**
     * 批量删除预测结果
     * 
     * @param insectIds 需要删除的预测结果主键
     * @return 结果
     */
    @Override
    public int deletePredictByInsectIds(Integer[] insectIds)
    {
        return predictMapper.deletePredictByInsectIds(insectIds);
    }

    /**
     * 删除预测结果信息
     * 
     * @param insectId 预测结果主键
     * @return 结果
     */
    @Override
    public int deletePredictByInsectId(Integer insectId)
    {
        return predictMapper.deletePredictByInsectId(insectId);
    }
}
