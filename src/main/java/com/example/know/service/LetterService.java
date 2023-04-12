package com.example.know.service;

import com.example.know.util.AjaxResult;

/**
 * 私信信息
 *
 * @author bookWorm
 */
public interface LetterService {

    /**
     * 获取私信
     *
     * @param username 用户名
     * @param start    起始
     * @param number   数量
     * @return
     */
    public AjaxResult getLetterBuUser(String username, int start, int number);

    /**
     * 忽悠未读私信统计
     *
     * @param username 用户名
     * @return
     */
    public AjaxResult getUnreadLetterCount(String username);

    /**
     * @param username  用户
     * @param recipient 接收者id
     * @param content   内容
     * @return
     */
    public boolean sendLetter(String username, int recipient, String content);


}
