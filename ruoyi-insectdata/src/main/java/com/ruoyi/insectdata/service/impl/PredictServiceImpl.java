package com.ruoyi.insectdata.service.impl;

import java.text.ParseException;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import com.ruoyi.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.insectdata.mapper.PredictMapper;
import com.ruoyi.insectdata.domain.Predict;
import com.ruoyi.insectdata.service.IPredictService;

import static com.ruoyi.insectdata.utils.DateUtils.*;

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
     * 按日期查询某时间段内某昆虫每天的数量
     *
     * @param
     * @return 每日对应的昆虫数量
     */
    @Override
    public int[] selectPredictByDatePeriod(Date start, Date end, Integer insectId) throws ParseException {
        //处理传入的时间
        Date s = clear(start);
        Date e = theMax(end);
        int days = daysBetween(s,e);
        System.out.println("days:"+days);
        //对应时间段的数量数组
        int[] nums = new int[days];
        Arrays.fill(nums,0);
        for(int i = 0 ; i < days ; i++){
            if(predictMapper.selectPredictByDate(s,addAndSubtractDaysByCalendar(s,1),insectId) != null){
                nums[i] = predictMapper.selectPredictByDate(s,addAndSubtractDaysByCalendar(s,1),insectId);
                System.out.println("i:"+i +"nums[i]:"+nums[i]);
            }
            s = addAndSubtractDaysByCalendar(s,1);
        }
        System.out.println("nums:"+Arrays.toString(nums));
//        identificationMapper.selectInsectByDate(start,insectId);
        return nums;
    }

    /**
     * 按日期查询某天某昆虫每小时的预测结果
     *
     * @param
     * @return 每小时对应的预测结果
     */
    @Override
    public int[] selectPredictByDate(Date date, Integer insectId) throws ParseException {
        int[] hour = new int[24];
        Date tmp = clear(date);
        System.out.println("=================================================================================================================tmp:" + tmp.toString());
        for(int i = 0 ; i < hour.length ; i++){
            Date tmp2 = hourMax(tmp);
//            if(i == hour.length - 1){
//                if(identificationMapper.selectInsectByDate(tmp,theMax(date),insectId) != null){
//                    hour[i] = identificationMapper.selectInsectByDate(tmp,theMax(date),insectId);
//                }else{
//                    hour[i] = 0;
//                }
//            }else{
//                System.out.println(identificationMapper);
            if(predictMapper.selectPredictByDate(tmp,tmp2,insectId) != null){
                hour[i] = predictMapper.selectPredictByDate(tmp,tmp2,insectId);
            }else {
                hour[i] = 0;
            }
//                System.out.println("---------------------------------------"+identificationMapper.selectInsectByDate(tmp, tmp2, insectId));
//            }
            tmp = hourMin(addHour(tmp2,1));
        }
        return hour;
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
