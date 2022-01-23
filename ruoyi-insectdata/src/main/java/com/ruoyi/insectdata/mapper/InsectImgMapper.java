package com.ruoyi.insectdata.mapper;

import java.util.List;
import com.ruoyi.insectdata.domain.InsectImg;

/**
 * 昆虫图片Mapper接口
 * 
 * @author zmh
 * @date 2021-08-29
 */
public interface InsectImgMapper 
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
     * 批量新增昆虫图片
     *
     * @param insectImgList 昆虫图片列表
     * @return 结果
     */
    public int batchInsertImg(List<InsectImg> insectImgList);

    /**
     * 修改昆虫图片
     * 
     * @param insectImg 昆虫图片
     * @return 结果
     */
    public int updateInsectImg(InsectImg insectImg);

    /**
     * 删除昆虫图片
     * 
     * @param insectId 昆虫图片主键
     * @return 结果
     */
    public int deleteInsectImgByInsectId(Long insectId);

    /**
     * 批量删除昆虫图片
     * 
     * @param insectIds 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteInsectImgByInsectIds(Long[] insectIds);
}
