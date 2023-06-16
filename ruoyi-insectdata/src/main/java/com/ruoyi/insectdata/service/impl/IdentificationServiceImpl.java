package com.ruoyi.insectdata.service.impl;

import com.ruoyi.insectdata.domain.Identification;
import com.ruoyi.insectdata.mapper.IdentificationMapper;
import com.ruoyi.insectdata.service.IIdentificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static com.ruoyi.insectdata.utils.DateUtils.*;

//import com.ruoyi.common.utils.DateUtils;

/**
 * 识别结果Service业务层处理
 * 
 * @author zmh
 * @date 2021-08-29
 */
@Service
public class IdentificationServiceImpl implements IIdentificationService 
{
    @Autowired
    private IdentificationMapper identificationMapper;

    /**
     * 查询识别结果
     * 
     * @param dataId 识别结果主键
     * @return 识别结果
     */
    @Override
    public Identification selectIdentificationByDataId(Integer dataId)
    {
        return identificationMapper.selectIdentificationByDataId(dataId);
    }

    /**
     * 查询识别结果列表
     * 
     * @param identification 识别结果
     * @return 识别结果
     */
    @Override
    public List<Identification> selectIdentificationList(Identification identification)
    {
        return identificationMapper.selectIdentificationList(identification);
    }


    /**
     * 按日期查询某时间段内某昆虫每天的数量
     *
     * @param
     * @return 每日对应的昆虫数量
     */
    @Override
    public int[] selectInsectByDatePeriod(Date start, Date end, Integer insectId) throws ParseException {
        //处理传入的时间
        Date s = clear(start);
        Date e = theMax(end);
        int days = daysBetween(s,e);
        System.out.println("days:"+days);
        //对应时间段的数量数组
        int[] nums = new int[days];
        Arrays.fill(nums,0);
        for(int i = 0 ; i < days ; i++){
            if(identificationMapper.selectInsectByDate(s,addAndSubtractDaysByCalendar(s,1),insectId) != null){
                nums[i] = identificationMapper.selectInsectByDate(s,addAndSubtractDaysByCalendar(s,1),insectId);
                System.out.println("i:"+i +"nums[i]:"+nums[i]);
            }
            s = addAndSubtractDaysByCalendar(s,1);
        }
        System.out.println("nums:"+Arrays.toString(nums));
//        identificationMapper.selectInsectByDate(start,insectId);
        return nums;
    }

//    /**
//     * 根据日期查询昆虫数量
//     *
//     * @param
//     * @return 每日对应的昆虫数量
//     */
//    @Override
//    public Integer selectInsectByDate(Date date, Integer insectId) throws ParseException {
//        return identificationMapper.selectInsectByDate(clear(date),theMax(date),insectId);
//    }

    /**
     * 按日期查询某天某昆虫每小时的数量
     *
     * @param
     * @return 每小时对应的昆虫数量
     */
    @Override
    public int[] selectInsectByDate(Date date, Integer insectId) throws ParseException {
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
                   if(identificationMapper.selectInsectByDate(tmp,tmp2,insectId) != null){
                       hour[i] = identificationMapper.selectInsectByDate(tmp,tmp2,insectId);
                   }else {
                       hour[i] = 0;
                   }
//                System.out.println("---------------------------------------"+identificationMapper.selectInsectByDate(tmp, tmp2, insectId));
//            }
            tmp = hourMin(addHour(tmp2,1));
        }
        return hour;
    }

    @Override
    public List<Identification> selectIdentificationDetail(Integer dataId) {
        return identificationMapper.selectIdentificationDetail(dataId);
    }

    /**
     * 新增识别结果
     * 
     * @param identification 识别结果
     * @return 结果
     */
    @Override
    public int insertIdentification(Identification identification)
    {
        return identificationMapper.insertIdentification(identification);
    }

    /**
     * 修改识别结果
     * 
     * @param identification 识别结果
     * @return 结果
     */
    @Override
    public int updateIdentification(Identification identification)
    {
        return identificationMapper.updateIdentification(identification);
    }

    /**
     * 批量删除识别结果
     * 
     * @param dataIds 需要删除的识别结果主键
     * @return 结果
     */
    @Override
    public int deleteIdentificationByDataIds(Integer[] dataIds)
    {
        return identificationMapper.deleteIdentificationByDataIds(dataIds);
    }

    /**
     * 删除识别结果信息
     * 
     * @param dataId 识别结果主键
     * @return 结果
     */
    @Override
    public int deleteIdentificationByDataId(Integer dataId)
    {
        return identificationMapper.deleteIdentificationByDataId(dataId);
    }

    @Override
    public int deleteIdentificationByDataIdAndInsectId(Integer dataId, Integer insectId) {
        return identificationMapper.deleteIdentificationByDataIdAndInsectId(dataId,insectId);
    }

    @Override
    public boolean hasIdentification(Integer dataId, Integer insectId) {
        int result = identificationMapper.hasIdentification(dataId, insectId);
        return result > 0 ? true : false;
    }
}
