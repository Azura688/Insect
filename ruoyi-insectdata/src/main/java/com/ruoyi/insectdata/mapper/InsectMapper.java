package com.ruoyi.insectdata.mapper;

import java.util.List;
import com.ruoyi.insectdata.domain.Insect;

/**
 * 昆虫Mapper接口
 * 
 * @author zmh
 * @date 2021-08-29
 */
public interface InsectMapper 
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
     * 查询昆虫目、科、种列表
     *
     * @param type 类别
     * @return 昆虫集合
     */
    public List<Insect> selectInsectSpeciesList(String type);

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
     * 删除昆虫
     * 
     * @param insectId 昆虫主键
     * @return 结果
     */
    public int deleteInsectByInsectId(Integer insectId);

    /**
     * 批量删除昆虫
     * 
     * @param insectIds 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteInsectByInsectIds(Integer[] insectIds);

    /**
     * 是否存在子节点
     *
     * @param insectId 昆虫ID
     * @return 结果
     */
    public int hasChildByInsectId(Integer insectId);
}
