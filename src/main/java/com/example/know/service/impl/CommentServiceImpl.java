package com.example.know.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.know.dao.CommentDao;
import com.example.know.dao.UserDao;
import com.example.know.entity.Comment;
import com.example.know.entity.CommentAndAuthor;
import com.example.know.entity.CommentTree;
import com.example.know.entity.User;
import com.example.know.service.CommentService;
import com.example.know.util.AjaxResult;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author bookWorm
 */
@Service
public class CommentServiceImpl implements CommentService {

    @Resource
    private CommentDao commentDao;

    @Resource
    private UserDao userDao;

    @Override
    public AjaxResult getCommentById(int id, int start, int number, char type) {
        if (type != '0' || type != '1') {
            return new AjaxResult(AjaxResult.Type.PARAM_TYPE_ERROR, "类型错误");
        }
        LambdaQueryWrapper lambdaQueryWrapper = new LambdaQueryWrapper();
        lambdaQueryWrapper.eq("commentable_type", type);
        lambdaQueryWrapper.eq("commentable_id", id);
        long count = commentDao.selectCount(lambdaQueryWrapper);
        PageHelper.startPage(start, number);
        List<Comment> commentList = commentDao.selectList(lambdaQueryWrapper);
        Map map = new HashMap();
        map.put("count", count);
        map.put("commentList", commentList);
        return AjaxResult.success(map);
    }

    @Override
    public List getPostComment(int postId) {
        List<CommentAndAuthor> comments = commentDao.getCommentByPostId(postId);
        List<CommentTree> commentTrees = new ArrayList<>(comments.size());
        for (CommentAndAuthor comment : comments) {
            CommentTree commentTree = new CommentTree();
            commentTree.setComment(comment);
            commentTrees.add(commentTree);
        }
        for (CommentTree commentTree : commentTrees) {
            getCommentChildren(commentTree);
        }
        return commentTrees;
    }
    private void getCommentChildren(CommentTree commentTree) {
        List<CommentAndAuthor> commentList = commentDao.getCommentByComment(commentTree.getComment().getCommentId());
        List<CommentTree> commentTrees = new ArrayList<>(commentList.size());
        for (CommentAndAuthor comment : commentList) {
            CommentTree commentTree1 = new CommentTree();
            commentTree1.setComment(comment);
            commentTrees.add(commentTree1);
        }
        commentTree.setChildren(commentTrees);
        for (CommentTree commentTree1 : commentTrees) {
            getCommentChildren(commentTree1);
        }
    }
    @Override
    public boolean insertComment(String username, int commentId, String content, char type) {
        User user = userDao.selectOfUserName(username);
        Comment comment = new Comment();
        comment.setCommentableId(commentId);
        comment.setCommentableType(type);
        comment.setCommentBy(user.getUserId());
        comment.setContent(content);
        int insert = commentDao.insert(comment);
        return insert == 1;
    }
}
