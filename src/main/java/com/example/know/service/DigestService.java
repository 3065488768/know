package com.example.know.service;

import com.example.know.util.AjaxResult;

/**
 * 书摘
 *
 * @author bookWorm
 */
public interface DigestService {

    /**
     * 获取推荐的书摘
     *
     * @return
     */
    public AjaxResult getDigestOfRecommend();

    /**
     * 返回表内数据量
     *
     * @return
     */
    public Long getCountNumber();

    /**
     * 获取书摘伴随书籍id
     *
     * @param bookId   书籍id
     * @param username 用户名
     * @param start    起始
     * @param number   数量
     * @return
     */
    public AjaxResult getDigestByBookId(int bookId, String username, int start, int number);

    /**
     * 喜欢或收藏书摘，用户喜欢或取消喜欢书摘
     *
     * @param username 用户名
     * @param digestId 书摘id
     * @param type     类型 0-喜欢 1-收藏
     * @return
     */
    public AjaxResult digestByUser(String username, int digestId, int type);
}
