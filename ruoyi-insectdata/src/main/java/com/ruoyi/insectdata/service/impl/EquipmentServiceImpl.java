package com.ruoyi.insectdata.service.impl;

import java.util.List;
import com.ruoyi.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.insectdata.mapper.EquipmentMapper;
import com.ruoyi.insectdata.domain.Equipment;
import com.ruoyi.insectdata.service.IEquipmentService;

/**
 * 设备Service业务层处理
 * 
 * @author zmh
 * @date 2021-10-21
 */
@Service
public class EquipmentServiceImpl implements IEquipmentService 
{
    @Autowired
    private EquipmentMapper equipmentMapper;

    /**
     * 查询设备
     * 
     * @param id 设备主键
     * @return 设备
     */
    @Override
    public Equipment selectEquipmentById(Integer id)
    {
        return equipmentMapper.selectEquipmentById(id);
    }

    /**
     * 查询设备列表
     * 
     * @param equipment 设备
     * @return 设备
     */
    @Override
    public List<Equipment> selectEquipmentList(Equipment equipment)
    {
        return equipmentMapper.selectEquipmentList(equipment);
    }

    /**
     * 新增设备
     * 
     * @param equipment 设备
     * @return 结果
     */
    @Override
    public int insertEquipment(Equipment equipment)
    {
        equipment.setCreateTime(DateUtils.getNowDate());
        return equipmentMapper.insertEquipment(equipment);
    }

    /**
     * 修改设备
     * 
     * @param equipment 设备
     * @return 结果
     */
    @Override
    public int updateEquipment(Equipment equipment)
    {
        equipment.setUpdateTime(DateUtils.getNowDate());
        return equipmentMapper.updateEquipment(equipment);
    }

    /**
     * 批量删除设备
     * 
     * @param ids 需要删除的设备主键
     * @return 结果
     */
    @Override
    public int deleteEquipmentByIds(Integer[] ids)
    {
        return equipmentMapper.deleteEquipmentByIds(ids);
    }

    /**
     * 删除设备信息
     * 
     * @param id 设备主键
     * @return 结果
     */
    @Override
    public int deleteEquipmentById(Integer id)
    {
        return equipmentMapper.deleteEquipmentById(id);
    }
}
