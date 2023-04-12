package com.example.know.service.impl;

import com.alibaba.fastjson2.util.DateUtils;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.know.dao.*;
import com.example.know.entity.*;
import com.example.know.enumeration.Setting;
import com.example.know.service.BookPathService;
import com.example.know.service.BookService;
import com.example.know.util.AjaxResult;
import com.github.pagehelper.PageHelper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * 书籍业务实现层
 *
 * @author bookWorm
 */
@Service
public class BookServiceImpl implements BookService {
    @Resource
    private BookDao bookDao;
    @Resource
    private BookTypeDao bookTypeDao;

    @Resource
    private DigestDao digestDao;

    @Resource
    private UserDao userDao;

    @Resource
    private BookPathDao bookPathDao;

    @Resource
    private PostDao postDao;

    @Resource
    private ExpectDao expectDao;

    @Override
    public AjaxResult getBookByNewOrder() {
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("status", '0');
        queryWrapper.orderByDesc("create_time");
        PageHelper.startPage(0, 12);
        List list = bookDao.selectList(queryWrapper);
        return AjaxResult.success(list);
    }

    @Override
    public AjaxResult getBookByHotOrder() {
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("status", '0');
        queryWrapper.orderByDesc("heat_degree");
        PageHelper.startPage(0, 12);
        List list = bookDao.selectList(queryWrapper);
        return AjaxResult.success(list);
    }

    @Override
    public AjaxResult getBookByLikeOrder() {
        return AjaxResult.success(bookDao.getBookByLikeOrder());
    }

    @Override
    public AjaxResult getBookByCollectionOrder() {
        return AjaxResult.success(bookDao.getBookByCollectionOrder());
    }

    @Override
    public AjaxResult getBookByExpectOrder() {
        PageHelper.startPage(0, 12);
        return AjaxResult.success(bookDao.getBookByExpectOrder());
    }

    @Override
    public AjaxResult getBookOfRecommend() {
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("status", '0');
        queryWrapper.orderByDesc("create_time");
        PageHelper.startPage(0, 20);
        List list = bookDao.selectList(queryWrapper);
        return AjaxResult.success(list);
    }

    @Override
    public AjaxResult getBookType() {
        QueryWrapper queryWrapper = new QueryWrapper();
        return AjaxResult.success(bookTypeDao.selectList(queryWrapper));
    }

    @Override
    public AjaxResult getBookByCondition(int bookType, int status, int order, String startTime, String endTime, int startPage) {
        QueryWrapper queryWrapper = new QueryWrapper();
        if (bookType != 0) {
            queryWrapper.eq("book_type_id", bookType);
        }
        if (status >= 0) queryWrapper.eq("status", status);
        if (order == 1) {
            queryWrapper.orderByDesc("create_time");
        } else if (order == 2) {
            queryWrapper.orderByDesc("heat_degree");
        }
        if (!(startTime == null || startTime.isBlank()) && !(endTime == null || endTime.isBlank())) {
            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH-mm-ss");
            queryWrapper.apply(true, "create_time between '" + dateFormat.format(DateUtils.parseDate(startTime + " 00:00:00")) + "' and '" + dateFormat.format(DateUtils.parseDate(endTime + " 23:59:59")) + "'");
        }
        int size = bookDao.selectList(queryWrapper).size();

        //每页20条
        if (startPage > 0) PageHelper.startPage(startPage, Setting.SHOWNUMBER);
        List list = bookDao.selectList(queryWrapper);
        Map map = new HashMap();
        map.put("size", size);
        map.put("books", list);
        return AjaxResult.success(map);
    }

    @Override
    public AjaxResult updateBookType(Book book) {
        return AjaxResult.success(bookDao.updateById(book));
    }

    @Override
    public AjaxResult addBook(Book book) {
        return AjaxResult.success(bookDao.insert(book));
    }

    @Override
    public Long getCountNumber() {
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("status", '0');
        return bookDao.selectCount(queryWrapper);
    }

    @Override
    public AjaxResult getBookById(int bookId) {
        Book book = bookDao.selectById(bookId);
        BookType bookType = bookTypeDao.selectById(book.getBookTypeId());
        QueryWrapper queryWrapperByDigest = new QueryWrapper();
        queryWrapperByDigest.eq("book_id", book.getBookId());
        Long digestNumber = digestDao.selectCount(queryWrapperByDigest);
        QueryWrapper queryWrapperByReferences = new QueryWrapper();
        queryWrapperByReferences.eq("status", '0');
        queryWrapperByReferences.eq("book_id", bookId);
        Long reference = postDao.selectCount(queryWrapperByReferences);
        String contributor = userDao.selectById(book.getContributor()).getNickName();
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("book_id", bookId);
        Map map = new HashMap();
        map.put("book", book);
        map.put("bookType", bookType);
        map.put("contributor", contributor);
        map.put("digestNumber", digestNumber);
        map.put("references", reference);
        map.put("linkValidity", bookPathDao.selectCount(queryWrapper) == 0 ? false : true);
        return AjaxResult.success(map);
    }

    @Override
    public AjaxResult getBookClassification() {
        List bookClassification = bookDao.getBookClassification();

        return AjaxResult.success(bookClassification);
    }

    @Override
    public int insertBook(Book book) {
        return bookDao.insert(book);
    }

    @Override
    public boolean isSubscribeBook(String username, int bookId) {
        User user = userDao.selectOfUserName(username);
        if (Objects.isNull(user)) {
            return false;
        }
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("book_id", bookId);
        queryWrapper.eq("expect_by", user.getUserId());
        Long aLong = expectDao.selectCount(queryWrapper);
        if (aLong == 1l) {
            return true;
        }
        return false;
    }

    @Override
    public int subscribeBook(String username, int bookId) {
        User user = userDao.selectOfUserName(username);
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("book_id", bookId);
        queryWrapper.eq("expect_by", user.getUserId());
        Expect expect = expectDao.selectOne(queryWrapper);
        if (Objects.isNull(expect)) {
            Expect expect1 = new Expect();
            expect1.setBookId(bookId);
            expect1.setExpectBy(user.getUserId());
            expectDao.insert(expect1);
            return 1;
        } else {
            expectDao.deleteById(expect.getExpectId());
            return -1;
        }
    }

    @Override
    public List expectBookAndCount() {
        return bookDao.expectBookAndCount();
    }

    @Override
    public long getExpectBookCount() {
        return expectDao.selectCount(null);
    }

    @Override
    public int updateBook(Book book) {
        Book book1 = bookDao.selectById(book.getBookId());
        book1.setBookName(book.getBookName());
        book1.setBookTypeId(book.getBookTypeId());
        book1.setStatus(book.getStatus());
        book1.setAuthor(book.getAuthor());
        book1.setRemark(book.getRemark());
        return bookDao.updateById(book1);
    }
}
