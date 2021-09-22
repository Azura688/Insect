package com.ruoyi.web.controller.insectdata;

import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.insectdata.domain.InsectImg;
import com.ruoyi.insectdata.service.IInsectImgService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 昆虫图片Controller
 * 
 * @author zmh
 * @date 2021-08-29
 */
@Api(tags = "昆虫图片信息")
@RestController
@RequestMapping("/insectdata/insectImg")
public class InsectImgController extends BaseController
{
    @Autowired
    private IInsectImgService insectImgService;

    /**
     * 查询昆虫图片列表
     */
    @ApiOperation("查询昆虫图片列表")
    @PreAuthorize("@ss.hasPermi('insectdata:insectImg:list')")
    @GetMapping("/list")
    public TableDataInfo list(InsectImg insectImg)
    {
        startPage();
        List<InsectImg> list = insectImgService.selectInsectImgList(insectImg);
        return getDataTable(list);
    }

    /**
     * 导出昆虫图片列表
     */
    @ApiOperation("导出昆虫图片列表")
    @PreAuthorize("@ss.hasPermi('insectdata:insectImg:export')")
    @Log(title = "昆虫图片", businessType = BusinessType.EXPORT)
    @GetMapping("/export")
    public AjaxResult export(InsectImg insectImg)
    {
        List<InsectImg> list = insectImgService.selectInsectImgList(insectImg);
        ExcelUtil<InsectImg> util = new ExcelUtil<InsectImg>(InsectImg.class);
        return util.exportExcel(list, "昆虫图片数据");
    }

    /**
     * 获取昆虫图片详细信息
     */
    @ApiOperation("获取昆虫图片详细信息")
    @PreAuthorize("@ss.hasPermi('insectdata:insectImg:query')")
    @GetMapping(value = "/{insectId}")
    public AjaxResult getInfo(@PathVariable("insectId") Long insectId)
    {
        return AjaxResult.success(insectImgService.selectInsectImgByInsectId(insectId));
    }

    /**
     * 新增昆虫图片
     */
    @ApiOperation("新增昆虫图片")
    @PreAuthorize("@ss.hasPermi('insectdata:insectImg:add')")
    @Log(title = "昆虫图片", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody InsectImg insectImg)
    {
        return toAjax(insectImgService.insertInsectImg(insectImg));
    }

    /**
     * 修改昆虫图片
     */
    @ApiOperation("修改昆虫图片")
    @PreAuthorize("@ss.hasPermi('insectdata:insectImg:edit')")
    @Log(title = "昆虫图片", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody InsectImg insectImg)
    {
        return toAjax(insectImgService.updateInsectImg(insectImg));
    }

    /**
     * 删除昆虫图片
     */
    @ApiOperation("删除昆虫图片")
    @PreAuthorize("@ss.hasPermi('insectdata:insectImg:remove')")
    @Log(title = "昆虫图片", businessType = BusinessType.DELETE)
	@DeleteMapping("/{insectIds}")
    public AjaxResult remove(@PathVariable Long[] insectIds)
    {
        return toAjax(insectImgService.deleteInsectImgByInsectIds(insectIds));
    }
}
