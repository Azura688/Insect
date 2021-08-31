package com.ruoyi.insectdata.service;

import java.util.List;
import com.ruoyi.insectdata.domain.InsectImg;

/**
 * 昆虫图片Service接口
 * 
 * @author zmh
 * @date 2021-08-29
 */
public interface IInsectImgService 
{
    /**
     * 查询昆虫图片
     * 
     * @param insectId 昆虫图片主键
     * @return 昆虫图片
     */
    public InsectImg selectInsectImgByInsectId(Long insectId);

    /**
     * 查询昆虫图片列表
     * 
     * @param insectImg 昆虫图片
     * @return 昆虫图片集合
     */
    public List<InsectImg> selectInsectImgList(InsectImg insectImg);

    /**
     * 新增昆虫图片
     * 
     * @param insectImg 昆虫图片
     * @return 结果
     */
    public int insertInsectImg(InsectImg insectImg);

    /**
     * 修改昆虫图片
     * 
     * @param insectImg 昆虫图片
     * @return 结果
     */
    public int updateInsectImg(InsectImg insectImg);

    /**
     * 批量删除昆虫图片
     * 
     * @param insectIds 需要删除的昆虫图片主键集合
     * @return 结果
     */
    public int deleteInsectImgByInsectIds(Long[] insectIds);

    /**
     * 删除昆虫图片信息
     * 
     * @param insectId 昆虫图片主键
     * @return 结果
     */
    public int deleteInsectImgByInsectId(Long insectId);
}
