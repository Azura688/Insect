package com.ruoyi.web.controller.insectdata;

import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.insectdata.domain.Identification;
import com.ruoyi.insectdata.service.IIdentificationService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 识别结果Controller
 * 
 * @author zmh
 * @date 2021-08-29
 */
@Api(tags = "识别结果信息")
@RestController
@RequestMapping("/insectdata/identification")
public class IdentificationController extends BaseController
{
    @Autowired
    private IIdentificationService identificationService;

    /**
     * 查询识别结果列表
     */
    @ApiOperation("查询识别结果列表")
    @PreAuthorize("@ss.hasPermi('insectdata:identification:list')")
    @GetMapping("/list")
    public TableDataInfo list(Identification identification)
    {
        startPage();
        List<Identification> list = identificationService.selectIdentificationList(identification);
        return getDataTable(list);
    }

    /**
     * 导出识别结果列表
     */
    @ApiOperation("导出识别结果列表")
    @PreAuthorize("@ss.hasPermi('insectdata:identification:export')")
    @Log(title = "识别结果", businessType = BusinessType.EXPORT)
    @GetMapping("/export")
    public AjaxResult export(Identification identification)
    {
        List<Identification> list = identificationService.selectIdentificationList(identification);
        ExcelUtil<Identification> util = new ExcelUtil<Identification>(Identification.class);
        return util.exportExcel(list, "识别结果数据");
    }

    /**
     * 获取识别结果详细信息
     */
    @ApiOperation("获取识别结果详细信息")
    @PreAuthorize("@ss.hasPermi('insectdata:identification:query')")
    @GetMapping(value = "/{dataId}")
    public AjaxResult getInfo(@PathVariable("dataId") Integer dataId)
    {
        return AjaxResult.success(identificationService.selectIdentificationByDataId(dataId));
    }

    /**
     *查询指定data的具体昆虫识别结果
     */
    @ApiOperation("查询指定data的具体昆虫识别结果")
    @GetMapping(value = "/detail/{dataID}")
    public TableDataInfo selectIdentificationDetail(Integer dataId){
        List<Identification> list = identificationService.selectIdentificationDetail(dataId);
        return getDataTable(list);
        //return identificationService.selectIdentificationDetail(dataId);
    }

    /**
     * 新增识别结果
     */
    @ApiOperation("新增识别结果")
    @PreAuthorize("@ss.hasPermi('insectdata:identification:add')")
    @Log(title = "识别结果", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody Identification identification)
    {
        return toAjax(identificationService.insertIdentification(identification));
    }

    /**
     * 修改识别结果
     */
    @ApiOperation("修改识别结果")
    @PreAuthorize("@ss.hasPermi('insectdata:identification:edit')")
    @Log(title = "识别结果", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody Identification identification)
    {
        return toAjax(identificationService.updateIdentification(identification));
    }

    /*public AjaxResult updataIdentifation(){


    }*/

    /**
     * 删除识别结果
     */
    @ApiOperation("删除识别结果")
    @PreAuthorize("@ss.hasPermi('insectdata:identification:remove')")
    @Log(title = "识别结果", businessType = BusinessType.DELETE)
	@DeleteMapping("/{dataIds}")
    public AjaxResult remove(@PathVariable Integer[] dataIds)
    {
        return toAjax(identificationService.deleteIdentificationByDataIds(dataIds));
    }
}
