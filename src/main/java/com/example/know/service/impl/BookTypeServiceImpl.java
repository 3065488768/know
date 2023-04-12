package com.example.know.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.example.know.dao.BookDao;
import com.example.know.dao.BookTypeDao;
import com.example.know.entity.Book;
import com.example.know.entity.BookType;
import com.example.know.service.BookTypeService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Objects;

@Service
public class BookTypeServiceImpl implements BookTypeService {

    @Resource
    private BookTypeDao bookTypeDao;

    @Resource
    private BookDao bookDao;

    @Override
    public int updateBookType(BookType bookType) {
        return bookTypeDao.updateById(bookType);
    }

    @Override
    public int deleteBookType(int bookTypeId) {
        BookType bookType = bookTypeDao.selectById(bookTypeId);
        if (Objects.isNull(bookType)) {
            return 0;
        } else {
            LambdaQueryWrapper<Book> lambdaQueryWrapper = new LambdaQueryWrapper<>();
            lambdaQueryWrapper.eq(Book::getBookTypeId, bookTypeId);
            Long aLong = bookDao.selectCount(lambdaQueryWrapper);
            if (aLong > 0) return -1;
            return bookTypeDao.deleteById(bookTypeId);
        }
    }
}
