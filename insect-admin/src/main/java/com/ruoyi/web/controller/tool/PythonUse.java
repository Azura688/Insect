package com.ruoyi.web.controller.tool;

import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.utils.file.FileUtils;
import com.ruoyi.insectdata.domain.Data;
import com.ruoyi.insectdata.mapper.DataMapper;
import com.ruoyi.insectdata.service.IDataService;
import com.ruoyi.web.controller.common.CommonController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.util.HashMap;

/**
 * @Classname PythonUse
 * @Description TODO
 * @Date 2023/5/3 16:19
 * @Created by 明慧
 */
public class PythonUse {



    @Autowired
    private IDataService dataService;



    //Anaconda系统变量路径
    private String condaPath = System.getenv("anaconda");
    //python模型路径
    private String pythonPath = "D:\\testPython\\cloud\\";

    private HashMap<Integer, String> insectMap = new HashMap<Integer, String>() {{
        put(0, "灰飞虱");
        put(1, "白背飞虱");
        put(2, "褐飞虱");
    }};



    /**
     *
     * @param dataId 要识别图片的id
     * @return
     */
    public HashMap<String, Integer> PythonModel(CommonController commonController, DataMapper dataMapper, Integer dataId){
        condaPath =condaPath.replace("\\","\\\\");
        System.out.println(condaPath);
        pythonPath =pythonPath.replace("\\","\\\\");
        System.out.println(pythonPath);
        HashMap<String, Integer> map = new HashMap<>();

        Data data = dataMapper.selectDataByDataId(dataId);
        String imgPath = data.getOriginalPicture();
        System.out.println(imgPath);
        try {
            Process proc = Runtime.getRuntime().exec(condaPath+"\\activate.bat insect && d: && cd " + pythonPath + "&& python detect.py --weights ./weights/best.pt --source " + imgPath);

            //print
            BufferedReader in = new BufferedReader(new InputStreamReader(proc.getInputStream(), "gbk"));
            BufferedReader error = new BufferedReader(new InputStreamReader(proc.getErrorStream(),"gbk"));

            StringBuilder sb= new StringBuilder();
            StringBuilder sbError= new StringBuilder();

            String line = null;
            String lineError = null;
            String resultPath = null;
            while ((line = in.readLine()) != null) {
                if(line.contains("Results saved to ")){
                    resultPath = line.substring(17);
                    resultPath = resultPath.replace("\\", "\\\\");
                    System.out.println("resultPath : " + resultPath);
                }
                sb.append(line);
                sb.append("\n");
            }
            System.out.println("sb:" + sb);

            while ((lineError= error.readLine()) != null) {
                sbError.append(lineError);
                sbError.append("\n");
            }
        System.out.println("sbError:" + sbError);
            in.close();
            error.close();
        System.out.println("finish");

            //更新识别后的图片
            String path = pythonPath + resultPath;
            File directory1 = new File(path);
            String file1 = directory1.list((dir, name) -> name.toLowerCase().endsWith(".jpg") || name.endsWith(".jpeg"))[0];
        System.out.println(file1);
            String tagPath = path + "\\\\" +file1;
        System.out.println("tagPath : " + tagPath);
            MultipartFile file = FileUtils.fileChange(tagPath);
            if(file == null){
                System.out.println("tagPath is null, cannot upload the file");
                return map;
            }

            AjaxResult ajaxResult = commonController.uploadFile(file);
        System.out.println(ajaxResult);
            String img = (String) ajaxResult.get("url");
            //String img = "http://81.71.138.29:59999" + ajaxResult.get("fileName");
        System.out.println(img);
            data.setTagPicture(img);
            dataMapper.updateData(data);


            //解析txt文件
            String labelPath = path + "\\labels\\";
            System.out.println(labelPath);
            File directory2 = new File(labelPath);
            String file2 = directory2.list((dir, name) -> name.toLowerCase().endsWith(".txt"))[0];
            System.out.println(file2);
            String txtPath = labelPath + file2;
            map = analyseTXT(txtPath);

        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("error");
        }
        return map;
    }

    /**
     * 分析txt文件
     * @param txtPath
     * @return HashMap
     */
    public HashMap<String, Integer> analyseTXT(String txtPath){
        HashMap<String, Integer> map = new HashMap<>();
        try (BufferedReader br = new BufferedReader(new FileReader(txtPath))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] values = line.split(" ");
                int num = Integer.parseInt(values[0]);
                System.out.println("The first number in this line is " + num);
                map.put(insectMap.get(num), map.getOrDefault(insectMap.get(num), 0) + 1);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        for (String key : map.keySet()) {
            System.out.println("key: " + key + "; value: " + map.get(key));
        }

        return map;
    }

}
