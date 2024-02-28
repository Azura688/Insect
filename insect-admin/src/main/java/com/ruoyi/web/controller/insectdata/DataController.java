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
import com.ruoyi.web.controller.common.CommonController;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Date;
import java.util.List;

/**
 * 识别数据Controller
 *
 * @author zmh
 * @date 2021-08-29
 */
@Api(tags = "数据信息")
@CrossOrigin
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

    @Autowired
    private CommonController commonController;

    @Autowired
    private PythonUseController pythonUseController;
    /**
     * 查询识别数据列表
     */
    @ApiOperation("查询数据")
    //@PreAuthorize("@ss.hasPermi('insectdata:data:list')")
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
     * 批量增加数据图片
     *
     * @param originalPictures 数据图片列表
     * @param equipmentId 设备号
     * @param photoTime 拍摄时间
     * @param photoArea 拍摄地点
     * @return 结果
     */
    @ApiOperation("批量增加数据图片")
    @PostMapping("/batchData")
    public AjaxResult batchData(String[] originalPictures, Integer equipmentId, Date photoTime, String photoArea,String bugtype){
        return toAjax(dataService.batchData(originalPictures,equipmentId,photoTime,photoArea,bugtype));
    }

    @ApiOperation("批量上传和添加数据图片")
    @PostMapping("/upLoadAndInsertData")
    @Log(title = "批量上传和添加数据图片", businessType = BusinessType.INSERT)
    public AjaxResult[] upLoadAndInsertImg(Integer equipmentId, Date photoTime, String photoArea,String bugtype, @RequestParam("file") MultipartFile[] files) throws Exception {
        int num = files.length;
        AjaxResult[] ajaxResults = commonController.batchUploadFile(files);
        String[] originalPictures = new String[num];
        int i = 0;
        for (AjaxResult ajax : ajaxResults) {
            ajax.put("equipmentId", equipmentId);
            originalPictures[i] = "https://7039vz8591.imdo.co" + ajax.get("fileName");
            i++;
        }
        dataService.batchData(originalPictures,equipmentId,photoTime,photoArea,bugtype);
        return ajaxResults;
    }
    @ApiOperation("机器自动批量上传和添加数据图片并且进行识别")
    @PostMapping("/upLoadImgAndidentify")
    @Log(title = "机器自动批量上传和添加数据图片并且进行识别", businessType = BusinessType.INSERT)
    public AjaxResult[] upLoadImgAndidentify(Integer equipmentId, Date photoTime, String photoArea,String bugtype, @RequestParam("file") MultipartFile[] files) throws Exception {
        int num = files.length;
        AjaxResult[] ajaxResults = commonController.batchUploadFile(files);
        String[] originalPictures = new String[num];
        int i = 0;
        for (AjaxResult ajax : ajaxResults) {
            ajax.put("equipmentId", equipmentId);
            originalPictures[i] = "https://7039vz8591.imdo.co" + ajax.get("fileName");
            i++;
        }
        dataService.batchData(originalPictures,equipmentId,photoTime,photoArea,bugtype);
        Integer[] dataids=dataService.selectDataIDs(originalPictures);
        for(int x:dataids) {
            if (bugtype.equals('1'))
            {
                pythonUseController.BigpythonModel(x);
            }
            else {
                pythonUseController.SmallpythonModel(x);
            }
        }
        return ajaxResults;
    }

    /**
     * 修改识别数据
     */
    @ApiOperation("修改识别数据")
    @PreAuthorize("@ss.hasPermi('insectdata:data:edit')")
    @Log(title = "识别数据", businessType = BusinessType.UPDATE)
    @PostMapping("/edit")
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
	@PostMapping("/delete/{dataIds}")
    public AjaxResult remove(@PathVariable Integer[] dataIds)
    {
        identificationService.deleteIdentificationByDataIds(dataIds);
        return toAjax(dataService.deleteDataByDataIds(dataIds));
    }

    /**
     * 根据用户地区查询相同地区设备的识别数据
     */
    @ApiOperation("根据用户地区查询相同地区设备的识别数据")
    @GetMapping("/listByAddress")
    public TableDataInfo listByAddress(String address){
        startPage();
        List<Data> list = dataService.selectDataByUserAddress(address);
        return getDataTable(list);
    }

    /**
     * 根据用户地区和时间查询识别数据
     */
    @ApiOperation("根据用户地区和时间查询识别数据")
    @GetMapping("/listByAddressAndTime")
    public TableDataInfo listByAddressAndTime(String address, Date photoTime){
        startPage();
        List<Data> list = dataService.selectDataByUserAddressAndTime(address, photoTime);
        return getDataTable(list);
    }

}
