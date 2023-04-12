package com.example.know.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.know.entity.Plate;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @author bookWorm
 */
@Mapper
public interface PlateDao extends BaseMapper<Plate> {

    /**
     * 获取首页的版块信息
     *
     * @return
     */
    public List getPlateInformation();

    /**
     * 获取版块的综合得分排行
     *
     * @return
     */
    public List getPlateByRanking();
}
