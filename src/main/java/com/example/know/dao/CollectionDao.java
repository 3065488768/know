package com.example.know.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.know.entity.BookType;
import com.example.know.entity.Collection;
import org.apache.ibatis.annotations.Mapper;

/**
 * 收藏
 *
 * @author bookWorm
 */
@Mapper
public interface CollectionDao extends BaseMapper<Collection> {
}
