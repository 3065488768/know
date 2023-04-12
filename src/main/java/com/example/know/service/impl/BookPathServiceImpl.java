package com.example.know.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.know.dao.BookPathDao;
import com.example.know.entity.BookPath;
import com.example.know.service.BookPathService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Objects;

/**
 * @author bookWorm
 */
@Service
public class BookPathServiceImpl implements BookPathService {

    @Resource
    private BookPathDao bookPathDao;

    @Override
    public String getPathOfValid(int bookId) {
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("book_id", bookId);
        queryWrapper.eq("status", '0');
        return bookPathDao.selectOne(queryWrapper).getPath();
    }

    @Override
    public int insert(BookPath bookPath) {
        bookPath.setStatus('1');
        return bookPathDao.insert(bookPath);
    }

    @Override
    public List<BookPath> getPath(int bookId) {
        LambdaQueryWrapper<BookPath> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(BookPath::getBookId, bookId);
        return bookPathDao.selectList(lambdaQueryWrapper);
    }

    @Override
    public int updatePath(int bookId, String path) {
        LambdaQueryWrapper<BookPath> lambdaQueryWrapper = new LambdaQueryWrapper();
        lambdaQueryWrapper.eq(BookPath::getBookId, bookId).and(bookPathLambdaQueryWrapper -> {
            bookPathLambdaQueryWrapper.eq(BookPath::getStatus, '0');
        });

        LambdaQueryWrapper<BookPath> lambdaQueryWrapper1 = new LambdaQueryWrapper();
        lambdaQueryWrapper1.eq(BookPath::getBookId, bookId).and(bookPathLambdaQueryWrapper -> {
            bookPathLambdaQueryWrapper.eq(BookPath::getPath, path);
        });

        BookPath bookPath = bookPathDao.selectOne(lambdaQueryWrapper);

        if (!Objects.isNull(bookPath)) {
            bookPath.setStatus('1');
            bookPathDao.update(bookPath, lambdaQueryWrapper);
        }
        BookPath bookPath1 = bookPathDao.selectOne(lambdaQueryWrapper1);
        bookPath1.setStatus('0');
        bookPathDao.update(bookPath1, lambdaQueryWrapper1);
        return 1;
    }
}
