package com.ruoyi.web.controller.insectdata;

import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.insectdata.domain.Insect;
import com.ruoyi.insectdata.service.IInsectService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 昆虫Controller
 * 
 * @author zmh
 * @date 2021-08-29
 */
@Api(tags = "昆虫信息")
@RestController
@RequestMapping("/insectdata/insect")
public class InsectController extends BaseController
{
    @Autowired
    private IInsectService insectService;

    /**
     * 查询昆虫列表
     */
    @ApiOperation("查询昆虫列表")
    @PreAuthorize("@ss.hasPermi('insectdata:insect:list')")
    @GetMapping("/list")
    public AjaxResult list(Insect insect)
    {
        List<Insect> list = insectService.selectInsectList(insect);
        return AjaxResult.success(list);
    }

    /**
     * 查询昆虫种列表
     */
    @ApiOperation("查询昆虫种列表")
    @PreAuthorize("@ss.hasPermi('insectdata:insect:list')")
    @GetMapping("/listSpecies")
    public AjaxResult listSpecies()
    {
        List<Insect> list = insectService.selectInsectSpeciesList();
        return AjaxResult.success(list);
    }

    /**
     * 导出昆虫列表
     */
    @ApiOperation("导出昆虫列表")
    @PreAuthorize("@ss.hasPermi('insectdata:insect:export')")
    @Log(title = "昆虫", businessType = BusinessType.EXPORT)
    @GetMapping("/export")
    public AjaxResult export(Insect insect)
    {
        List<Insect> list = insectService.selectInsectList(insect);
        ExcelUtil<Insect> util = new ExcelUtil<Insect>(Insect.class);
        return util.exportExcel(list, "昆虫数据");
    }

    /**
     * 获取昆虫详细信息
     */
    @ApiOperation("获取昆虫详细信息")
    @PreAuthorize("@ss.hasPermi('insectdata:insect:query')")
    @GetMapping(value = "/{insectId}")
    public AjaxResult getInfo(@PathVariable("insectId") Integer insectId)
    {
        return AjaxResult.success(insectService.selectInsectByInsectId(insectId));
    }

    /**
     * 新增昆虫
     */
    @ApiOperation("新增昆虫")
    @PreAuthorize("@ss.hasPermi('insectdata:insect:add')")
    @Log(title = "昆虫", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody Insect insect)
    {
        return toAjax(insectService.insertInsect(insect));
    }

    /**
     * 修改昆虫
     */
    @ApiOperation("修改昆虫")
    @PreAuthorize("@ss.hasPermi('insectdata:insect:edit')")
    @Log(title = "昆虫", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody Insect insect)
    {
        return toAjax(insectService.updateInsect(insect));
    }

    /**
     * 删除昆虫
     */
    @ApiOperation("删除昆虫")
    @PreAuthorize("@ss.hasPermi('insectdata:insect:remove')")
    @Log(title = "昆虫", businessType = BusinessType.DELETE)
	@DeleteMapping("/{insectId}")
    public AjaxResult remove(@PathVariable Integer insectId)
    {
        if (insectService.hasChildByInsectId(insectId)){
            return AjaxResult.error("存在下级昆虫,不允许删除");
        }
        return toAjax(insectService.deleteInsectByInsectId(insectId));
    }
}
