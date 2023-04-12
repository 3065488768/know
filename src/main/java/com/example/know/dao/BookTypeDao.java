package com.example.know.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.know.entity.BookType;
import org.apache.ibatis.annotations.Mapper;

/**
 * 书籍类别
 *
 * @author bookWorm
 */
@Mapper
public interface BookTypeDao extends BaseMapper<BookType> {
}
