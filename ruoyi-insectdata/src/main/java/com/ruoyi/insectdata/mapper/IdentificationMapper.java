package com.ruoyi.insectdata.mapper;

import java.util.List;
import java.util.Date;
import com.ruoyi.insectdata.domain.Identification;
import org.apache.ibatis.annotations.Param;

/**
 * 识别结果Mapper接口
 * 
 * @author zmh
 * @date 2021-08-29
 */
public interface IdentificationMapper 
{
    /**
     * 查询识别结果
     * 
     * @param dataId 识别结果主键
     * @return 识别结果
     */
    public Identification selectIdentificationByDataId(Integer dataId);

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
     * 根据日期查询昆虫数量
     *
     * @param
     * @return 每日对应的昆虫数量
     */
    public Integer selectInsectByDate(@Param("start") Date start, @Param("end") Date end, @Param("insectId") Integer insectId);


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
     * 删除识别结果
     * 
     * @param dataId 识别结果主键
     * @return 结果
     */
    public int deleteIdentificationByDataId(Integer dataId);

    /**
     * 批量删除识别结果
     * 
     * @param dataIds 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteIdentificationByDataIds(Integer[] dataIds);

    /**
     * 删除识别结果信息
     *
     * @param dataId 识别结果主键
     * @param insectId
     * @return 结果
     */
    public int deleteIdentificationByDataIdAndInsectId(@Param("dataId") Integer dataId, @Param("insectId") Integer insectId);


}
