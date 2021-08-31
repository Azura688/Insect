package com.ruoyi.web.controller.insectdata;

import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.insectdata.domain.Predict;
import com.ruoyi.insectdata.service.IPredictService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 预测结果Controller
 * 
 * @author zmh
 * @date 2021-08-29
 */
@RestController
@RequestMapping("/insectdata/predict")
public class PredictController extends BaseController
{
    @Autowired
    private IPredictService predictService;

    /**
     * 查询预测结果列表
     */
    @PreAuthorize("@ss.hasPermi('insectdata:predict:list')")
    @GetMapping("/list")
    public TableDataInfo list(Predict predict)
    {
        startPage();
        List<Predict> list = predictService.selectPredictList(predict);
        return getDataTable(list);
    }

    /**
     * 导出预测结果列表
     */
    @PreAuthorize("@ss.hasPermi('insectdata:predict:export')")
    @Log(title = "预测结果", businessType = BusinessType.EXPORT)
    @GetMapping("/export")
    public AjaxResult export(Predict predict)
    {
        List<Predict> list = predictService.selectPredictList(predict);
        ExcelUtil<Predict> util = new ExcelUtil<Predict>(Predict.class);
        return util.exportExcel(list, "预测结果数据");
    }

    /**
     * 获取预测结果详细信息
     */
    @PreAuthorize("@ss.hasPermi('insectdata:predict:query')")
    @GetMapping(value = "/{insectId}")
    public AjaxResult getInfo(@PathVariable("insectId") Integer insectId)
    {
        return AjaxResult.success(predictService.selectPredictByInsectId(insectId));
    }

    /**
     * 新增预测结果
     */
    @PreAuthorize("@ss.hasPermi('insectdata:predict:add')")
    @Log(title = "预测结果", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody Predict predict)
    {
        return toAjax(predictService.insertPredict(predict));
    }

    /**
     * 修改预测结果
     */
    @PreAuthorize("@ss.hasPermi('insectdata:predict:edit')")
    @Log(title = "预测结果", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody Predict predict)
    {
        return toAjax(predictService.updatePredict(predict));
    }

    /**
     * 删除预测结果
     */
    @PreAuthorize("@ss.hasPermi('insectdata:predict:remove')")
    @Log(title = "预测结果", businessType = BusinessType.DELETE)
	@DeleteMapping("/{insectIds}")
    public AjaxResult remove(@PathVariable Integer[] insectIds)
    {
        return toAjax(predictService.deletePredictByInsectIds(insectIds));
    }
}
