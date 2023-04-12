package com.example.know.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.know.entity.Post;
import com.example.know.entity.SimplePost;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 简易处理帖子内容
 *
 * @author bookWorm
 */
@Mapper
public interface SimplePostDao extends BaseMapper<SimplePost> {
    /**
     * 模糊查询帖子
     *
     * @param parameter     搜索的关键字
     * @param type          查询标题-0、内容-1、标题和内容-2
     * @param plateId       板块类型数组
     * @param postType      0-普通 1-精华
     * @param rankType      最热-2、最新-1、默认-0
     * @param datetimeStart 起始日期
     * @param datetimeEnd   终止日期
     * @return
     */

    public List getFuzzySearchPost(int[] plateId, char postType, String parameter, int type, int rankType, String datetimeStart, String datetimeEnd);

    /**
     * 获取最新评论的帖子
     *
     * @return
     */
    public List getSimplePostOfLatestComments();

    /**
     * 获取帖子与相关作者的信息
     *
     * @param flag
     * @return
     */
    public List getSimplePostAndAuthor(int flag);

    /**
     * 根据书籍类型id获取帖子
     *
     * @param bookTypeId 书籍类型id
     * @return 返回
     */
    public List postIdByBookTypeId(int[] bookTypeId);
}

