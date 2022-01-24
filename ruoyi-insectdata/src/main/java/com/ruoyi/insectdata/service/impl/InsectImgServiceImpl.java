package com.ruoyi.insectdata.service.impl;

import java.util.ArrayList;
import java.util.List;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.insectdata.domain.Insect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.insectdata.mapper.InsectImgMapper;
import com.ruoyi.insectdata.domain.InsectImg;
import com.ruoyi.insectdata.service.IInsectImgService;

/**
 * 昆虫图片Service业务层处理
 * 
 * @author zmh
 * @date 2021-08-29
 */
@Service
public class InsectImgServiceImpl implements IInsectImgService 
{
    @Autowired
    private InsectImgMapper insectImgMapper;

    /**
     * 查询昆虫图片
     * 
     * @param insectId 昆虫图片主键
     * @return 昆虫图片
     */
    @Override
    public InsectImg selectInsectImgByInsectId(Long insectId)
    {
        return insectImgMapper.selectInsectImgByInsectId(insectId);
    }

    /**
     * 查询昆虫图片列表
     * 
     * @param insectImg 昆虫图片
     * @return 昆虫图片
     */
    @Override
    public List<InsectImg> selectInsectImgList(InsectImg insectImg)
    {
        return insectImgMapper.selectInsectImgList(insectImg);
    }

    /**
     * 新增昆虫图片
     * 
     * @param insectImg 昆虫图片
     * @return 结果
     */
    @Override
    public int insertInsectImg(InsectImg insectImg)
    {
        insectImg.setCreateTime(DateUtils.getNowDate());
        return insectImgMapper.insertInsectImg(insectImg);
    }

    /**
     * 批量新增昆虫图片
     *
     * @param insectId 昆虫id
     * @param imgs 昆虫图片列表
     * @return 结果
     */
    @Override
    public int batchInsertImg(Long insectId, String[] imgs) {
        List<InsectImg> list = new ArrayList<InsectImg>();
        for (String img : imgs){
            InsectImg insectImg = new InsectImg();
            insectImg.setInsectId(insectId);
            insectImg.setImg(img);
            insectImg.setCreateTime(DateUtils.getNowDate());
            list.add(insectImg);
        }
        if(list.size() > 0){
            return insectImgMapper.batchInsertImg(list);
        }else {
            return 0;
        }

    }

    /**
     * 修改昆虫图片
     * 
     * @param insectImg 昆虫图片
     * @return 结果
     */
    @Override
    public int updateInsectImg(InsectImg insectImg)
    {
        insectImg.setUpdateTime(DateUtils.getNowDate());
        return insectImgMapper.updateInsectImg(insectImg);
    }

    /**
     * 批量删除昆虫图片
     * 
     * @param insectImgs 需要删除的昆虫图片主键
     * @return 结果
     */
    @Override
    public int deleteInsectImgs(InsectImg[] insectImgs)
    {
        return insectImgMapper.deleteInsectImgs(insectImgs);
    }

    /**
     * 删除昆虫图片信息
     * 
     * @param insectId 昆虫图片主键
     * @return 结果
     */
    @Override
    public int deleteInsectImgByInsectId(Long insectId)
    {
        return insectImgMapper.deleteInsectImgByInsectId(insectId);
    }
}
