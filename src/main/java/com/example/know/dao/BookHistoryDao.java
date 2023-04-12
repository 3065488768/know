package com.example.know.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.know.entity.Book;
import com.example.know.entity.BookHistory;
import org.apache.ibatis.annotations.Mapper;

/**
 * 书籍历史
 *
 * @author bookWorm
 */
@Mapper
public interface BookHistoryDao extends BaseMapper<BookHistory> {
}
