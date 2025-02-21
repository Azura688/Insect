package com.ruoyi.insectdata.service;

import com.ruoyi.insectdata.domain.Identification;

import java.text.ParseException;
import java.util.Date;
import java.util.List;

/**
 * 识别结果Service接口
 * 
 * @author zmh
 * @date 2021-08-29
 */
public interface IIdentificationService 
{
    /**
     * 查询识别结果
     * 
     * @param dataId 识别结果主键
     * @return 识别结果
     */
    public Identification selectIdentificationByDataId(Integer dataId);

    /**
     * 按日期查询某时间段内某昆虫每天的数量
     *
     * @param
     * @return 每日对应的昆虫数量
     */
    public int[] selectInsectByDatePeriod(Date start, Date end, Integer insectId) throws ParseException;

//    /**
//     * 根据日期查询昆虫数量
//     *
//     * @param
//     * @return 每日对应的昆虫数量
//     */
//    public Integer selectInsectByDate(Date start,Integer insectId)throws ParseException;

    /**
     * 按日期查询某天某昆虫每小时的数量
     *
     * @param
     * @return 每小时对应的昆虫数量
     */
    public int[] selectInsectByDate(Date date, Integer insectId) throws ParseException ;

    /**
     * 查询识别结果列表
     * 
     * @param identification 识别结果
     * @return 识别结果集合
     */
    public List<Identification> selectIdentificationList(Identification identification);

    /**
     * 查询指定data的具体昆虫识别结果
     *
     * @param dataId
     * @return
     */
    public List<Identification> selectIdentificationDetail (Integer dataId);

    /**
     * 新增识别结果
     * 
     * @param identification 识别结果
     * @return 结果
     */
    public int insertIdentification(Identification identification);

    /**
     * 修改识别结果
     * 
     * @param identification 识别结果
     * @return 结果
     */
    public int updateIdentification(Identification identification);

    /**
     * 批量删除识别结果
     * 
     * @param dataIds 需要删除的识别结果主键集合
     * @return 结果
     */
    public int deleteIdentificationByDataIds(Integer[] dataIds);

    /**
     * 删除识别结果信息
     * 
     * @param dataId 识别结果主键
     * @return 结果
     */
    public int deleteIdentificationByDataId(Integer dataId);

    /**
     * 删除识别结果信息
     *
     * @param dataId 识别结果主键
     * @param insectId
     * @return 结果
     */
    public int deleteIdentificationByDataIdAndInsectId(Integer dataId, Integer insectId);

    /**
     * 是否存在图片与昆虫对应的识别结果
     */
    public boolean hasIdentification(Integer dataId, Integer insectId);

}
