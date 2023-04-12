package com.example.know.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.know.entity.Access;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * 访问帖子记录
 *
 * @author bookWorm
 */
@Mapper
public interface AccessDao extends BaseMapper<Access> {

    /**
     * 统计各个时段的访问人数
     */
    public List countNumberByTime();
}
