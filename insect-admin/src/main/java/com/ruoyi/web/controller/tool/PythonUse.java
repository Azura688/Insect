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
//    private String pythonPath = "D:\\CS\\Insect\\Python\\yolov5-5.0\\";
    private String smallpythonPath = "D:\\work\\yolov5-5.0.1\\";
    private String bigpythonPath="D:\\mask\\mask_rcnn2 (5)\\";

    private HashMap<Integer, String> smallinsectMap = new HashMap<Integer, String>() {{
        put(0, "灰飞虱");
        put(1, "白背飞虱");
        put(2, "褐飞虱");
    }};

    private HashMap<Integer, String> biginsectMap = new HashMap<Integer, String>() {{
        put(1, "草地贪夜蛾");
        put(2, "稻纵卷叶螟");
        put(3,"二化螟");
        put(4,"玉米螟");
        put(5,"粘虫");
    }};



    /**
     *
     * @param dataId 要识别图片的id
     * @return
     */
    public HashMap<String, Integer> SmallPythonModel(CommonController commonController, DataMapper dataMapper, Integer dataId){
        System.out.println("condaPath的路径"+condaPath);
        condaPath =condaPath.replace("\\","\\\\");
        System.out.println(condaPath);
        smallpythonPath =smallpythonPath.replace("\\","\\\\");
        System.out.println(smallpythonPath);
        HashMap<String, Integer> map = new HashMap<>();

        Data data = dataMapper.selectDataByDataId(dataId);
        String imgPath = data.getOriginalPicture();
        System.out.println(imgPath);
        try {
            // Anaconda 启动 调用python模型对此进行分析
            Process proc = Runtime.getRuntime().exec(condaPath+"\\activate.bat base && d: && cd " + smallpythonPath + "&& python detect.py --weights ./weights/best_no_windows.pt --source " + imgPath);
//            Process proc = Runtime.getRuntime().exec(condaPath+"\\activate.bat yolov5 && d: && cd " + pythonPath + "&& python detect.py --weights ./weights/best_no_windows.pt --source " + imgPath);

            //print
            //读取进程的输出流以及错误流并且转换为字符流，并指定字符编码 gbk
            BufferedReader in = new BufferedReader(new InputStreamReader(proc.getInputStream(), "gbk"));
            BufferedReader error = new BufferedReader(new InputStreamReader(proc.getErrorStream(),"gbk"));

            //用于创建可变的字符串对象，方便后续将读取到的内容进行拼接
            StringBuilder sb= new StringBuilder();
            StringBuilder sbError= new StringBuilder();

            String line = null;
            String lineError = null;
            String resultPath = null;
            while ((line = in.readLine()) != null) {
                if(line.contains("Results saved to ")){
                    //从索引17开始一直到字符串的末尾 截取这一段字符串
                    resultPath = line.substring(17);
                    resultPath = resultPath.replace("\\", "\\\\");
                    System.out.println("resultPath : " + resultPath);
                }
                sb.append(line);
                sb.append("\n");
                //将读取到的每行内容加入到sb可变字符串对象中 并在每行结尾添加一个换行符“\n”，已保持每行内容的格式
            }
            System.out.println("sb:" + sb);

            while ((lineError= error.readLine()) != null) {
                sbError.append(lineError);
                sbError.append("\n");
            }
        System.out.println("sbError:" + sbError);
            in.close();
            error.close();
            //调用close()方法，可以释放流占用的资源并确保数据完全被写入或读取。
        System.out.println("finish");

            //更新识别后的图片
            String path = smallpythonPath + resultPath;
            File directory1 = new File(path);
            String file1 = directory1.list((dir, name) -> name.toLowerCase().endsWith(".jpg") || name.endsWith(".jpeg"))[0];
        System.out.println(file1);
            String tagPath = path + "\\\\" +file1;
        System.out.println("tagPath : " + tagPath);
        //将tagPath路径下的文件转换为MultipartFile类型的文件
            MultipartFile file = FileUtils.fileChange(tagPath);
            if(file == null){
                System.out.println("tagPath is null, cannot upload the file");
                return map;
            }
            //将文件上传，并将返回的结果保存到ajaxResult变量中
            AjaxResult ajaxResult = commonController.uploadFile(file);
           System.out.println(ajaxResult);
           //通过拼接字符串的方式生成图片的访问路径img
            String img = "https://7039vz8591.imdo.co" + (String) ajaxResult.get("fileName");
//            String img = "http://localhost:59999" + ajaxResult.get("fileName");
        System.out.println(img);
            data.setTagPicture(img);
            dataMapper.updateData(data);


            //解析txt文件
            String labelPath = path + "\\labels\\";
            System.out.println(labelPath);
            File directory2 = new File(labelPath);

            //判断labelPath文件夹（也就是directory2）里面是否有文件
            File[] fileLists = directory2.listFiles();
            if(fileLists.length == 0){//没有文件，说明没有识别结果
                return map;
            }

            String file2 = directory2.list((dir, name) -> name.toLowerCase().endsWith(".txt"))[0];
            System.out.println(file2);
            String txtPath = labelPath + file2;
            map = smallanalyseTXT(txtPath);

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
    public HashMap<String, Integer> smallanalyseTXT(String txtPath){
        HashMap<String, Integer> map = new HashMap<>();
        try (BufferedReader br = new BufferedReader(new FileReader(txtPath))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] values = line.split(" ");
                int num = Integer.parseInt(values[0]);
                System.out.println("The first number in this line is " + num);
                map.put(smallinsectMap.get(num), map.getOrDefault(smallinsectMap.get(num), 0) + 1);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        for (String key : map.keySet()) {
            System.out.println("key: " + key + "; value: " + map.get(key));
        }

        return map;
    }

    public HashMap<String, Integer> BigPythonModel(CommonController commonController, DataMapper dataMapper, Integer dataId){
        System.out.println("condaPath的路径"+condaPath);
        condaPath =condaPath.replace("\\","\\\\");
        System.out.println(condaPath);
        bigpythonPath =bigpythonPath.replace("\\","\\\\");
        System.out.println(bigpythonPath);
        HashMap<String, Integer> map = new HashMap<>();

        Data data = dataMapper.selectDataByDataId(dataId);
        String imgPath = data.getOriginalPicture();
        System.out.println(imgPath);
        try {
            // Anaconda 启动 调用python模型对此进行分析
            Process proc = Runtime.getRuntime().exec(condaPath+"\\activate.bat zjm && d: && cd " + bigpythonPath + "&& python predict.py --source " + imgPath);
//            Process proc = Runtime.getRuntime().exec(condaPath+"\\activate.bat yolov5 && d: && cd " + pythonPath + "&& python detect.py --weights ./weights/best_no_windows.pt --source " + imgPath);

            //print
            //读取进程的输出流以及错误流并且转换为字符流，并指定字符编码 gbk
            BufferedReader in = new BufferedReader(new InputStreamReader(proc.getInputStream(), "gbk"));
            BufferedReader error = new BufferedReader(new InputStreamReader(proc.getErrorStream(),"gbk"));

            //用于创建可变的字符串对象，方便后续将读取到的内容进行拼接
            StringBuilder sb= new StringBuilder();
            StringBuilder sbError= new StringBuilder();

            String line = null;
            String lineError = null;
            String resultPath = null;
            while ((line = in.readLine()) != null) {
                if(line.contains("Results saved to ")){
                    //从索引18开始一直到字符串的末尾 截取这一段字符串
                    resultPath = line.substring(18);
                    resultPath = resultPath.replace("\\", "\\\\");
                    System.out.println("resultPath : " + resultPath);
                }
                sb.append(line);
                sb.append("\n");
                //将读取到的每行内容加入到sb可变字符串对象中 并在每行结尾添加一个换行符“\n”，已保持每行内容的格式
            }
            System.out.println("sb:" + sb);

            while ((lineError= error.readLine()) != null) {
                sbError.append(lineError);
                sbError.append("\n");
            }
            System.out.println("sbError:" + sbError);
            in.close();
            error.close();
            //调用close()方法，可以释放流占用的资源并确保数据完全被写入或读取。
            System.out.println("finish");

            //更新识别后的图片
            String path = bigpythonPath + resultPath;
            File directory1 = new File(path);
            String file1 = directory1.list((dir, name) -> name.toLowerCase().endsWith(".jpg") || name.endsWith(".jpeg"))[0];
            System.out.println(file1);
            String tagPath = path + "\\\\" +file1;
            System.out.println("tagPath : " + tagPath);
            //将tagPath路径下的文件转换为MultipartFile类型的文件
            MultipartFile file = FileUtils.fileChange(tagPath);
            if(file == null){
                System.out.println("tagPath is null, cannot upload the file");
                return map;
            }
            //将文件上传，并将返回的结果保存到ajaxResult变量中
            AjaxResult ajaxResult = commonController.uploadFile(file);
            System.out.println(ajaxResult);
            //通过拼接字符串的方式生成图片的访问路径img
            String img = "https://7039vz8591.imdo.co" + (String) ajaxResult.get("fileName");
//            String img = "http://localhost:59999" + ajaxResult.get("fileName");
            System.out.println(img);
            data.setTagPicture(img);
            dataMapper.updateData(data);


            //解析txt文件
            String labelPath = path;
            System.out.println(labelPath);
            File directory2 = new File(labelPath);

            //判断labelPath文件夹（也就是directory2）里面是否有文件
            File[] fileLists = directory2.listFiles();
            if(fileLists.length == 0){//没有文件，说明没有识别结果
                return map;
            }

            String file2 = directory2.list((dir, name) -> name.toLowerCase().endsWith(".txt"))[0];
            System.out.println(file2);
            String txtPath = labelPath + "\\\\" +file2;
            map = biganalyseTXT(txtPath);

        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("error");
        }
        return map;
    }
    public HashMap<String, Integer> biganalyseTXT(String txtPath){
        HashMap<String, Integer> map = new HashMap<>();
        try (BufferedReader br = new BufferedReader(new FileReader(txtPath))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] values = line.split(" ");
                int num = Integer.parseInt(values[0]);
                System.out.println("The first number in this line is " + num);
                map.put(biginsectMap.get(num), map.getOrDefault(biginsectMap.get(num), 0) + 1);
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
