package com.example.know.service;

import com.example.know.entity.Book;
import com.example.know.util.AjaxResult;

import java.util.List;

/**
 * 处理书籍业务
 *
 * @author bookWorm
 */
public interface BookService {

    /**
     * 获取最新的书籍
     *
     * @return
     */
    public AjaxResult getBookByNewOrder();

    /**
     * 获取最热的书籍
     *
     * @return
     */
    public AjaxResult getBookByHotOrder();

    /**
     * 获取最多喜欢的书籍
     *
     * @return
     */
    public AjaxResult getBookByLikeOrder();

    /**
     * 获取最多收藏的书籍
     *
     * @return
     */
    public AjaxResult getBookByCollectionOrder();

    /**
     * 获取最期待的书籍
     *
     * @return
     */
    public AjaxResult getBookByExpectOrder();

    /**
     * 获取推荐的书籍
     *
     * @return
     */
    public AjaxResult getBookOfRecommend();

    /**
     * 获取书籍类别信息
     *
     * @return
     */
    public AjaxResult getBookType();

    /**
     * 依据条件进行返回书籍
     *
     * @param bookType  0-全部 后面对应书籍类别顺序
     * @param status    0-正常 1-审核中 2-筹措 3-下架
     * @param order     排序 0-默认 1-最新 2-最热
     * @param startTime 起始时间
     * @param endTime   终止时间
     * @param startPage 起始页码
     * @return
     */
    public AjaxResult getBookByCondition(int bookType, int status, int order, String startTime, String endTime, int startPage);

    /**
     * 更改书籍类型
     *
     * @param book 书籍信息
     * @return
     */
    public AjaxResult updateBookType(Book book);

    /**
     * 增加书籍信息
     *
     * @param book 书籍信息
     * @return
     */
    public AjaxResult addBook(Book book);

    /**
     * 返回表内数据量
     *
     * @return
     */
    public Long getCountNumber();

    /**
     * 根据书籍id获取相关信息
     *
     * @param bookId 书籍id
     * @return
     */
    public AjaxResult getBookById(int bookId);

    /**
     * 获取书类型的书籍
     *
     * @return 类型1 - 书籍1，书籍2，书籍3
     * 类型2 = 书籍4，书籍5，书籍6
     */
    public AjaxResult getBookClassification();

    /**
     * 悬赏书籍
     *
     * @param book 书籍信息
     * @return 成功数量
     */
    public int insertBook(Book book);

    /**
     * 是否订阅书籍
     *
     * @param username 用户名
     * @param bookId   书籍id
     * @return
     */
    public boolean isSubscribeBook(String username, int bookId);


    /**
     * 订阅书籍
     *
     * @param username 用户
     * @param bookId   书籍id
     * @return
     */
    public int subscribeBook(String username, int bookId);

    /**
     * 获取期待的图书伴随着它的人数
     * return 期待的书籍的订阅人数
     */
    public List expectBookAndCount();

    /**
     * 获取期待书籍的数量
     */
    public long getExpectBookCount();

    public int updateBook(Book book);
}
