package com.example.know.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.know.entity.Comment;
import com.example.know.entity.CommentAndAuthor;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @author bookWorm
 */
@Mapper
public interface CommentDao extends BaseMapper<Comment> {

    /**
     * 获取指定postId的评论
     *
     * @param postId 帖子id
     * @return
     */
    public List<CommentAndAuthor> getCommentByPostId(int postId);

    /**
     * 获取指定commentId的评论
     *
     * @param commentId 评论id
     * @return
     */
    public List<CommentAndAuthor> getCommentByComment(int commentId);

}
