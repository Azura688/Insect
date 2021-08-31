package com.ruoyi.insectdata.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.insectdata.mapper.IdentificationMapper;
import com.ruoyi.insectdata.domain.Identification;
import com.ruoyi.insectdata.service.IIdentificationService;

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
