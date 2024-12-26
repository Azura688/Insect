package com.ruoyi.web.controller.insectdata;

import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.insectdata.domain.Data;
import com.ruoyi.insectdata.domain.Identification;
import com.ruoyi.insectdata.mapper.DataMapper;
import com.ruoyi.insectdata.mapper.InsectMapper;
import com.ruoyi.insectdata.service.IIdentificationService;
import com.ruoyi.web.controller.common.CommonController;
import com.ruoyi.web.controller.tool.PythonUse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.HashMap;

/**
 * @Classname PythonUseController
 * @Description TODO
 * @Date 2023/5/3 16:18
 * @Created by 明慧
 */
@Api(tags = "python模型")
@RestController
@RequestMapping("/pythonUse")
public class PythonUseController {

    @Autowired
    private DataMapper dataMapper;

    @Autowired
    private InsectMapper insectMapper;

    @Autowired
    private IIdentificationService identificationService;

    @Autowired
    private CommonController commonController;

    @GetMapping("/test")
    public AjaxResult test(@RequestParam("dataId") Integer dataId) {
        return AjaxResult.success(dataMapper.selectDataByDataId(dataId));
    }

    /**
     * 调用python模型进行识别 小虫yolov
     */
    @ApiOperation("调用python模型进行识别")
    @PostMapping("/SmallpythonModel/{dataId}")
    public AjaxResult SmallpythonModel(@PathVariable Integer dataId) throws IOException {
        System.out.println("test Successful!");
        Data data = dataMapper.selectDataByDataId(dataId);
        System.out.println(data.getDataId());
        System.out.println(data.getOriginalPicture());


        PythonUse pythonUse = new PythonUse();
        HashMap<String, Integer> resultsMap = pythonUse.SmallPythonModel(commonController, dataMapper, dataId);
        for (String key : resultsMap.keySet()) {
            int insectId = insectMapper.selectInsectIdByInsectName(key);
            Identification identification = new Identification();
            identification.setDataId(dataId);
            identification.setInsectId(insectId);
            identification.setNumber(resultsMap.get(key));
            System.out.println("Small!!!!!!!");
            System.out.println(identificationService.hasIdentification(dataId, insectId));

            //如果已经有对应的识别结果,修改结果，否则添加
            if(identificationService.hasIdentification(dataId, insectId)){
                identificationService.updateIdentification(identification);
            }else {
                identificationService.insertIdentification(identification);
            }

        }

        return AjaxResult.success(resultsMap);
    }

    /**
     * 调用python模型进行识别 大虫MaskRcnn
     */
    @ApiOperation("调用python模型进行识别")
    @PostMapping("/BigpythonModel/{dataId}")
    public AjaxResult BigpythonModel(@PathVariable Integer dataId) throws IOException {
        System.out.println("test Successful!");
        Data data = dataMapper.selectDataByDataId(dataId);
        System.out.println(data.getDataId());
        System.out.println(data.getOriginalPicture());


        PythonUse pythonUse = new PythonUse();
        HashMap<String, Integer> resultsMap = pythonUse.BigPythonModel(commonController, dataMapper, dataId);
        for (String key : resultsMap.keySet()) {
            int insectId = insectMapper.selectInsectIdByInsectName(key);
            Identification identification = new Identification();
            identification.setDataId(dataId);
            identification.setInsectId(insectId);
            identification.setNumber(resultsMap.get(key));
            System.out.println("Big!!!!!!!");
            System.out.println(identificationService.hasIdentification(dataId, insectId));

            //如果已经有对应的识别结果,修改结果，否则添加
            if(identificationService.hasIdentification(dataId, insectId)){
                identificationService.updateIdentification(identification);
            }else {
                identificationService.insertIdentification(identification);
            }

        }

        return AjaxResult.success(resultsMap);
    }
}
