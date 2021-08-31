package com.ruoyi.insectdata.service;

import java.util.List;
import com.ruoyi.insectdata.domain.Insect;

/**
 * 昆虫Service接口
 * 
 * @author zmh
 * @date 2021-08-29
 */
public interface IInsectService 
{
    /**
     * 查询昆虫
     * 
     * @param insectId 昆虫主键
     * @return 昆虫
     */
    public Insect selectInsectByInsectId(Integer insectId);

    /**
     * 查询昆虫列表
     * 
     * @param insect 昆虫
     * @return 昆虫集合
     */
    public List<Insect> selectInsectList(Insect insect);

    /**
     * 新增昆虫
     * 
     * @param insect 昆虫
     * @return 结果
     */
    public int insertInsect(Insect insect);

    /**
     * 修改昆虫
     * 
     * @param insect 昆虫
     * @return 结果
     */
    public int updateInsect(Insect insect);

    /**
     * 批量删除昆虫
     * 
     * @param insectIds 需要删除的昆虫主键集合
     * @return 结果
     */
    public int deleteInsectByInsectIds(Integer[] insectIds);

    /**
     * 删除昆虫信息
     * 
     * @param insectId 昆虫主键
     * @return 结果
     */
    public int deleteInsectByInsectId(Integer insectId);
}
