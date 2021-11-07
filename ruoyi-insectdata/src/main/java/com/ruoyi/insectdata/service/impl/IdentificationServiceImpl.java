package com.ruoyi.insectdata.service.impl;

import java.util.List;
import java.util.Arrays;
import java.util.Date;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.insectdata.mapper.IdentificationMapper;
import com.ruoyi.insectdata.domain.Identification;
import com.ruoyi.insectdata.service.IIdentificationService;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

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
    //喵
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

    /**
     * 根据日期查询昆虫数量
     *
     * @param
     * @return 每日对应的昆虫数量
     */
    @Override
    public Integer selectInsectByDate(Date date, Integer insectId) throws ParseException {
        return identificationMapper.selectInsectByDate(clear(date),theMax(date),insectId);
    }

    //日期加减指定的天数
    public static Date addAndSubtractDaysByCalendar(Date dateTime/*待处理的日期*/,int n/*加减天数*/){

        //日期格式
        SimpleDateFormat df=new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat dd=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        java.util.Calendar calstart = java.util.Calendar.getInstance();
        calstart.setTime(dateTime);

        calstart.add(java.util.Calendar.DAY_OF_WEEK, n);

        //System.out.println(df.format(calstart.getTime()));
        //System.out.println(dd.format(calstart.getTime()));
        return calstart.getTime();
    }

    //将时分秒，毫秒域清零
    public static Date clear(Date date){
        Calendar cal1 = Calendar.getInstance();
        cal1.setTime(date);
        cal1.set(Calendar.HOUR_OF_DAY, 0);
        cal1.set(Calendar.MINUTE, 0);
        cal1.set(Calendar.SECOND, 0);
        cal1.set(Calendar.MILLISECOND, 0);
        return date;
    }

    //将时分秒，毫秒域设成最大
    public static Date theMax(Date date){
        Calendar cal1 = Calendar.getInstance();
        cal1.setTime(date);
        cal1.set(Calendar.HOUR_OF_DAY,23);
        cal1.set(Calendar.MINUTE, 59);
        cal1.set(Calendar.SECOND, 59);
        cal1.set(Calendar.MILLISECOND, 999);
        return date;
    }

    public static int daysBetween(Date smdate,Date bdate) throws ParseException//计算两个日期之间的天数
    {
        SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
        smdate=sdf.parse(sdf.format(smdate));
        bdate=sdf.parse(sdf.format(bdate));
        Calendar cal = Calendar.getInstance();
        cal.setTime(smdate);
        long time1 = cal.getTimeInMillis();
        cal.setTime(bdate);
        long time2 = cal.getTimeInMillis();
        long between_days=(time2-time1)/(1000*3600*24);

        return Integer.parseInt(String.valueOf(between_days));
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
}
