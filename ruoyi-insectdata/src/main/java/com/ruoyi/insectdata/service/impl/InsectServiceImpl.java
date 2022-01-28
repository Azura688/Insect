package com.ruoyi.insectdata.service.impl;

import java.util.List;
import com.ruoyi.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.insectdata.mapper.InsectMapper;
import com.ruoyi.insectdata.domain.Insect;
import com.ruoyi.insectdata.service.IInsectService;

/**
 * 昆虫Service业务层处理
 * 
 * @author zmh
 * @date 2021-08-29
 */
@Service
public class InsectServiceImpl implements IInsectService 
{
    @Autowired
    private InsectMapper insectMapper;

    /**
     * 查询昆虫
     * 
     * @param insectId 昆虫主键
     * @return 昆虫
     */
    @Override
    public Insect selectInsectByInsectId(Integer insectId)
    {
        return insectMapper.selectInsectByInsectId(insectId);
    }

    /**
     * 查询昆虫列表
     * 
     * @param insect 昆虫
     * @return 昆虫
     */
    @Override
    public List<Insect> selectInsectList(Insect insect)
    {
        return insectMapper.selectInsectList(insect);
    }

    /**
     * 查询昆虫目、科、种列表
     *
     * @param type 类别
     * @return 昆虫集合
     */
    @Override
    public List<Insect> selectInsectSpeciesList(String type){
        return insectMapper.selectInsectSpeciesList(type);
    }

    /**
     * 新增昆虫
     * 
     * @param insect 昆虫
     * @return 结果
     */
    @Override
    public int insertInsect(Insect insect)
    {
        insect.setCreateTime(DateUtils.getNowDate());
        return insectMapper.insertInsect(insect);
    }

    /**
     * 修改昆虫
     * 
     * @param insect 昆虫
     * @return 结果
     */
    @Override
    public int updateInsect(Insect insect)
    {
        insect.setUpdateTime(DateUtils.getNowDate());
        return insectMapper.updateInsect(insect);
    }

    /**
     * 批量删除昆虫
     * 
     * @param insectIds 需要删除的昆虫主键
     * @return 结果
     */
    @Override
    public int deleteInsectByInsectIds(Integer[] insectIds)
    {
        return insectMapper.deleteInsectByInsectIds(insectIds);
    }

    /**
     * 删除昆虫信息
     * 
     * @param insectId 昆虫主键
     * @return 结果
     */
    @Override
    public int deleteInsectByInsectId(Integer insectId)
    {
        return insectMapper.deleteInsectByInsectId(insectId);
    }

    /**
     * 是否存在昆虫子节点
     *
     * @param insectId 昆虫ID
     * @return 结果
     */
    @Override
    public boolean hasChildByInsectId(Integer insectId) {
        int result = insectMapper.hasChildByInsectId(insectId);
        return result > 0 ? true : false;
    }
}
