package com.example.know.service;

import com.example.know.util.AjaxResult;

import java.util.List;

/**
 * 获取通知信息
 *
 * @author bookWorm
 */

public interface NoticeService {

    /**
     * 获取通知内容使用userId
     *
     * @param username 用户名
     * @param start    起始
     * @param number   数量
     * @return
     */
    public AjaxResult getNoticeByUser(String username, int start, int number);

    /**
     * 获取未读信息都数量
     *
     * @param username 用户名
     * @return
     */
    public AjaxResult getNoticeUnread(String username);

    /**
     * 获取指定用户的通知
     *
     * @param username 用户
     * @return
     */
    public List getNoticeByUser(String username);
}
