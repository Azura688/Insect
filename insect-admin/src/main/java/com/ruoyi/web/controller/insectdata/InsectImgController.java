package com.ruoyi.web.controller.insectdata;

import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.insectdata.domain.InsectImg;
import com.ruoyi.insectdata.service.IInsectImgService;
import com.ruoyi.web.controller.common.CommonController;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * 昆虫图片Controller
 * 
 * @author zmh
 * @date 2021-08-29
 */
@Api(tags = "昆虫图片信息")
@CrossOrigin
@RestController
@RequestMapping("/insectdata/insectImg")
public class InsectImgController extends BaseController
{
    @Autowired
    private IInsectImgService insectImgService;

    @Autowired
    private CommonController commonController;

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
    public AjaxResult add(InsectImg insectImg)
    {
        return toAjax(insectImgService.insertInsectImg(insectImg));
    }

    /**
     * 批量新增昆虫图片
     *
     * @param insectId 昆虫id
     * @param imgs 昆虫图片列表
     * @return 结果
     */
    @ApiOperation("批量新增昆虫图片")
    @PostMapping("/batchInsertImg")
    public AjaxResult batchInsertImg(Long insectId, String[] imgs){
        return toAjax(insectImgService.batchInsertImg(insectId,imgs));
    }

    /**
     * 批量上传和添加昆虫图片
     *
     * @param insectId 昆虫id
     * @param files 昆虫图片
     * @return 结果
     */
    @ApiOperation("批量上传和添加昆虫图片")
    @PostMapping("/upLoadAndInsertImg")
    public AjaxResult[] upLoadAndInsertImg(Long insectId, @RequestParam("file") MultipartFile[] files) throws Exception {
        int num = files.length;
        AjaxResult[] ajaxResults = commonController.batchUploadFile(files);
        String[] imgs = new String[num];
        int i = 0;
        for (AjaxResult ajax : ajaxResults) {
            ajax.put("insectId", insectId);
            imgs[i] = "https://4138y554s5.goho.co" + ajax.get("fileName");
            i++;
        }
        insectImgService.batchInsertImg(insectId, imgs);
        return ajaxResults;
    }


    /**
     * 批量添加昆虫图片
     * @param files
     * @return
     */
    /*@PostMapping("/upload")
    @ResponseBody
    public AjaxResult addFile(@RequestParam("files") MultipartFile[] files) {

        for(MultipartFile fileData : files) {
            // 上传
            //sysBusinessService.doUploadReturnContract(fileData);
        }
        return AjaxResult.success("上传成功！");

    }*/

    /**
     * 修改昆虫图片
     */
    @ApiOperation("修改昆虫图片")
    @PreAuthorize("@ss.hasPermi('insectdata:insectImg:edit')")
    @Log(title = "昆虫图片", businessType = BusinessType.UPDATE)
    @PostMapping("/edit")
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
	@PostMapping("/delete")
    public AjaxResult remove(@RequestBody InsectImg[] insectImgs)
    {
        return toAjax(insectImgService.deleteInsectImgs(insectImgs));
    }
}
