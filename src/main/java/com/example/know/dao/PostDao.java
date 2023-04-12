package com.example.know.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.know.entity.Post;
import com.example.know.entity.SimplePost;
import com.example.know.util.AjaxResult;
import org.apache.ibatis.annotations.Mapper;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author bookWorm
 */
@Mapper
public interface PostDao extends BaseMapper<Post> {

    /**
     * 批量修改用户类型
     *
     * @param postId 帖子id
     * @param status 处理类型 0-正常 1-审核 2-待审核 3-停用 4-删除
     * @return
     */
    public int batchModification(int[] postId, char status);

    /**
     * 获取完整post，书籍、作者等
     *
     * @param postId 帖子id
     */
    public Map getCompletePost(int postId);

    public List getPostList(char status, int orderBy, char postType, int startPage);
}
