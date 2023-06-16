package com.ruoyi.web.controller.insectdata;

import java.util.List;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.insectdata.domain.Equipment;
import com.ruoyi.insectdata.service.IEquipmentService;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 设备Controller
 * 
 * @author zmh
 * @date 2021-10-21
 */
@Api(tags = "设备信息")
@CrossOrigin
@RestController
@RequestMapping("/insectdata/equipment")
public class EquipmentController extends BaseController
{
    @Autowired
    private IEquipmentService equipmentService;

    /**
     * 查询设备列表
     */
    @ApiOperation("查询设备列表")
    @PreAuthorize("@ss.hasPermi('insectdata:equipment:list')")
    @GetMapping("/list")
    public TableDataInfo list(Equipment equipment)
    {
        startPage();
        List<Equipment> list = equipmentService.selectEquipmentList(equipment);
        return getDataTable(list);
    }

    /**
     * 导出设备列表
     */
    @ApiOperation("导出设备列表")
    @PreAuthorize("@ss.hasPermi('insectdata:equipment:export')")
    @Log(title = "设备", businessType = BusinessType.EXPORT)
    @GetMapping("/export")
    public AjaxResult export(Equipment equipment)
    {
        List<Equipment> list = equipmentService.selectEquipmentList(equipment);
        ExcelUtil<Equipment> util = new ExcelUtil<Equipment>(Equipment.class);
        return util.exportExcel(list, "设备数据");
    }

    /**
     * 获取设备详细信息
     */
    @ApiOperation("获取设备详细信息")
    @PreAuthorize("@ss.hasPermi('insectdata:equipment:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Integer id)
    {
        return AjaxResult.success(equipmentService.selectEquipmentById(id));
    }

    /**
     * 新增设备
     */
    @ApiOperation("新增设备")
    @PreAuthorize("@ss.hasPermi('insectdata:equipment:add')")
    @Log(title = "设备", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody Equipment equipment)
    {
        return toAjax(equipmentService.insertEquipment(equipment));
    }

    /**
     * 修改设备
     */
    @ApiOperation("修改设备")
    @PreAuthorize("@ss.hasPermi('insectdata:equipment:edit')")
    @Log(title = "设备", businessType = BusinessType.UPDATE)
    @PostMapping("/edit")
    public AjaxResult edit(@RequestBody Equipment equipment)
    {
        return toAjax(equipmentService.updateEquipment(equipment));
    }

    /**
     * 删除设备
     */
    @ApiOperation("删除设备")
    @PreAuthorize("@ss.hasPermi('insectdata:equipment:remove')")
    @Log(title = "设备", businessType = BusinessType.DELETE)
	@PostMapping("/delete/{ids}")
    public AjaxResult remove(@PathVariable Integer[] ids)
    {
        return toAjax(equipmentService.deleteEquipmentByIds(ids));
    }
}
