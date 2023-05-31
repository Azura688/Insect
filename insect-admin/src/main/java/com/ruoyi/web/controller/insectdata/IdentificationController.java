package com.ruoyi.web.controller.insectdata;

import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.insectdata.domain.Identification;
import com.ruoyi.insectdata.mapper.InsectMapper;
import com.ruoyi.insectdata.service.IIdentificationService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.util.Arrays;
import java.util.Date;
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

    @Autowired
    private InsectMapper insectMapper;

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
     * 根据dataId查询标记框及对应昆虫
     */
    /*public AjaxResult listMarkAndInsect(Integer dataId){

    }*/

    /**
     * 按起止日期查询某时间段内某昆虫每天的数量
     */
    @ApiOperation("按起止日期查询某时间段内某昆虫每天的数量")
    @PreAuthorize("@ss.hasPermi('insectdata:identification:list')")
    @GetMapping("/listByDatePeriod")
    public AjaxResult listByDatePeriod(Date start, Date end, Integer insectId) throws ParseException {
        //整型数组转换成字符串数组
        String arrString = Arrays.toString(identificationService.selectInsectByDatePeriod(start,end,insectId));
        return AjaxResult.success(arrString);
    }

    /**
     * 按日期查询某天某昆虫每小时的数量
     */
    @ApiOperation("按日期查询某天某昆虫每小时的数量")
    @PreAuthorize("@ss.hasPermi('insectdata:identification:list')")
    @GetMapping("/listByDate")
    public AjaxResult listByDate(Date date,Integer insectId) throws ParseException {
        //整型数组转换成字符串数组
        String arrString = Arrays.toString(identificationService.selectInsectByDate(date,insectId));
        return AjaxResult.success(arrString);
    }

//
//    /**
//     * 按日期查询某天内某昆虫的数量
//     */
//    @ApiOperation("按日期查询某天内某昆虫的数量")
//    @PreAuthorize("@ss.hasPermi('insectdata:identification:list')")
//    @GetMapping("/listByDate")
//    public AjaxResult listByDate(Date date, Integer insectId) throws ParseException {
//        if(identificationService.selectInsectByDate(date,insectId) != null) {
//            return AjaxResult.success(identificationService.selectInsectByDate(date,insectId));
//        }else{
//            return AjaxResult.success(0);
//        }
//
//    }

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
     *查询指定data的具体昆虫识别结果(含标记框及对应昆虫)
     */
    @ApiOperation("查询指定data的具体昆虫识别结果")
    @GetMapping(value = "/detail/{dataId}")
    public TableDataInfo selectIdentificationDetail(@PathVariable("dataId") Integer dataId){
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
        System.out.println(insectMapper.selectTypeByInsectId(identification.getInsectId()));
        if(insectMapper.selectTypeByInsectId(identification.getInsectId()).equals("2")){
            return toAjax(identificationService.insertIdentification(identification));
        }else{
            return AjaxResult.error("请选择正确的昆虫！");
        }
    }

    /**
     * 修改识别结果
     */
    @ApiOperation("修改识别结果")
    @PreAuthorize("@ss.hasPermi('insectdata:identification:edit')")
    @Log(title = "识别结果", businessType = BusinessType.UPDATE)
    @PostMapping("/edit")
    public AjaxResult edit(@RequestBody Identification identification)
    {
        return toAjax(identificationService.updateIdentification(identification));
    }


    /**
     * 删除指定data的所有识别结果
     */
    @ApiOperation("删除指定data的所有识别结果")
    @PreAuthorize("@ss.hasPermi('insectdata:identification:remove')")
    @Log(title = "识别结果", businessType = BusinessType.DELETE)
	@PostMapping("/deleteAll/{dataIds}")
    public AjaxResult remove(@PathVariable Integer[] dataIds)
    {
        return toAjax(identificationService.deleteIdentificationByDataIds(dataIds));
    }

    /**
     * 删除data指定昆虫id的识别结果
     */
    @ApiOperation("删除data指定昆虫id的识别结果")
    @PreAuthorize("@ss.hasPermi('insectdata:identification:remove')")
    @Log(title = "识别结果", businessType = BusinessType.DELETE)
    @PostMapping("/delete/{dataId}&{insectId}")
    public AjaxResult delete(@RequestParam("dataId") Integer dataId, @RequestParam("insectId") Integer insectId)
    {
        return toAjax(identificationService.deleteIdentificationByDataIdAndInsectId(dataId,insectId));
    }
}
