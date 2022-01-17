package com.ruoyi.web.controller.insectdata;

import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.insectdata.domain.Data;
import com.ruoyi.insectdata.domain.Equipment;
import com.ruoyi.insectdata.service.IDataService;
import com.ruoyi.insectdata.service.IEquipmentService;
import com.ruoyi.insectdata.service.IIdentificationService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 识别数据Controller
 * 
 * @author zmh
 * @date 2021-08-29
 */
@Api(tags = "数据信息")
@RestController
@RequestMapping("/insectdata/data")
public class DataController extends BaseController
{
    @Autowired
    private IDataService dataService;

    @Autowired
    private IEquipmentService equipmentService;

    @Autowired
    private IIdentificationService identificationService;

    /**
     * 查询识别数据列表
     */
    @ApiOperation("查询数据")
    @PreAuthorize("@ss.hasPermi('insectdata:data:list')")
    @GetMapping("/list")
    public TableDataInfo list(Data data)
    {
        startPage();
        List<Data> list = dataService.selectDataList(data);
        return getDataTable(list);
    }

    /**
     * 导出识别数据列表
     */
    @ApiOperation("导出识别数据列表")
    @PreAuthorize("@ss.hasPermi('insectdata:data:export')")
    @Log(title = "识别数据", businessType = BusinessType.EXPORT)
    @GetMapping("/export")
    public AjaxResult export(Data data)
    {
        List<Data> list = dataService.selectDataList(data);
        ExcelUtil<Data> util = new ExcelUtil<Data>(Data.class);
        return util.exportExcel(list, "识别数据数据");
    }

    /**
     * 获取识别数据详细信息
     */
    @ApiOperation("获取识别数据详细信息")
    @PreAuthorize("@ss.hasPermi('insectdata:data:query')")
    @GetMapping(value = "/{dataId}")
    public AjaxResult getInfo(@PathVariable("dataId") Integer dataId)
    {
        return AjaxResult.success(dataService.selectDataByDataId(dataId));
    }

    /**
     * 新增识别数据
     */
    @ApiOperation("新增识别数据")
    @PreAuthorize("@ss.hasPermi('insectdata:data:add')")
    @Log(title = "识别数据", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody Data data)
    {
        Equipment result = equipmentService.selectEquipmentById(data.getEquipmentId());
        if(result == null){
            return AjaxResult.error("该设备不存在");
        }
        return toAjax(dataService.insertData(data));
    }

    /**
     * 修改识别数据
     */
    @ApiOperation("修改识别数据")
    @PreAuthorize("@ss.hasPermi('insectdata:data:edit')")
    @Log(title = "识别数据", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody Data data)
    {
        return toAjax(dataService.updateData(data));
    }

    /**
     * 删除识别数据
     */
    @ApiOperation("删除识别数据")
    @PreAuthorize("@ss.hasPermi('insectdata:data:remove')")
    @Log(title = "识别数据", businessType = BusinessType.DELETE)
	@DeleteMapping("/{dataIds}")
    public AjaxResult remove(@PathVariable Integer[] dataIds)
    {
        identificationService.deleteIdentificationByDataIds(dataIds);
        return toAjax(dataService.deleteDataByDataIds(dataIds));
    }
}
