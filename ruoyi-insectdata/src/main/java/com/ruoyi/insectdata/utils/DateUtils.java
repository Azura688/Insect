package com.ruoyi.insectdata.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * @Classname DateUtils
 * @Description TODO
 * @Date 2022/2/11 13:41
 * @Created by 明慧
 */
public class DateUtils {

    //将时分秒，毫秒域清零
    public static Date clear(Date date){
        Calendar cal1 = Calendar.getInstance();
        cal1.setTime(date);
        cal1.set(Calendar.HOUR_OF_DAY, 0);
        cal1.set(Calendar.MINUTE, 0);
        cal1.set(Calendar.SECOND, 0);
        cal1.set(Calendar.MILLISECOND, 0);
//        System.out.println("===================================date start: " + cal1.getTime().toString());
        return cal1.getTime();
    }

    //将日期加上h小时
    public static Date addHour(Date date,int h){
        Calendar cal1 = Calendar.getInstance();
        cal1.setTime(date);
        cal1.add(Calendar.HOUR_OF_DAY,h);
        System.out.println("===================================date addHour: " + cal1.getTime().toString());
        return cal1.getTime();
    }

    //将日期设为当前小时最大值
    public static Date hourMax(Date date){
        Calendar cal1 = Calendar.getInstance();
        cal1.setTime(date);
        cal1.set(Calendar.MINUTE, 59);
        cal1.set(Calendar.SECOND, 59);
        cal1.set(Calendar.MILLISECOND, 999);
        System.out.println("===================================date addHour: " + cal1.getTime().toString());
        return cal1.getTime();
    }

    //将日期设为当前小时最小值
    public static Date hourMin(Date date){
        Calendar cal1 = Calendar.getInstance();
        cal1.setTime(date);
        cal1.set(Calendar.MINUTE, 0);
        cal1.set(Calendar.SECOND, 0);
        cal1.set(Calendar.MILLISECOND, 0);
        System.out.println("===================================date addHour: " + cal1.getTime().toString());
        return cal1.getTime();
    }

    //将时分秒，毫秒域设成最大
    public static Date theMax(Date date){
        Calendar cal1 = Calendar.getInstance();
        cal1.setTime(date);
        cal1.set(Calendar.HOUR_OF_DAY,23);
        cal1.set(Calendar.MINUTE, 59);
        cal1.set(Calendar.SECOND, 59);
        cal1.set(Calendar.MILLISECOND, 999);
//        System.out.println("date end: " + cal1.getTime().toString());
        return cal1.getTime();
    }

    public static int daysBetween(Date smdate,Date bdate) throws ParseException//计算两个日期之间的天数
    {
        Calendar cal = Calendar.getInstance();
        cal.setTime(smdate);
        long time1 = cal.getTimeInMillis();
        cal.setTime(bdate);
        long time2 = cal.getTimeInMillis();
        long between_days=(time2-time1+1)/(1000*3600*24);

        return Integer.parseInt(String.valueOf(between_days));
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
}
