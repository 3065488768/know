package com.example.know.service;

import com.example.know.util.AjaxResult;

import java.util.List;

/**
 * 回复
 *
 * @author bookWorm
 */
public interface CommentService {

    /**
     * 根据id获取评论
     *
     * @param id     id
     * @param start  起始
     * @param number 数量
     * @param type   类型    '0' - 帖子  '1' - 评论
     * @return
     */
    public AjaxResult getCommentById(int id, int start, int number, char type);

    /**
     * 获取对于帖子的评论
     *
     * @param postId 帖子id
     * @return 返回帖子评论以及其子评论
     */
    public List getPostComment(int postId);

    /**
     * 插入评论
     *
     * @param username  用户名
     * @param commentId 评论对象id
     * @param content   评论类型
     * @param type      类型 0-帖子 1-评论
     * @ return
     */
    public boolean insertComment(String username, int commentId, String content, char type);
}
