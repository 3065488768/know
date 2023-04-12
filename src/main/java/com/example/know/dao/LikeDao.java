package com.example.know.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.know.entity.Like;
import org.apache.ibatis.annotations.Mapper;

/**
 * 喜欢
 *
 * @author bookWorm
 */
@Mapper
public interface LikeDao extends BaseMapper<Like> {
}
