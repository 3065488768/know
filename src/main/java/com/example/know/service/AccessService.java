package com.example.know.service;

import java.util.List;

/**
 * @author bookWorm
 */
public interface AccessService {

    /**
     * 获取帖子的访问数量
     *
     * @param postId 帖子id
     * @return
     */
    public Long accessByPostId(int postId);

    /**
     * 统计用户访问帖子的高峰期
     **/
    public List countNumberByTime();

    /**
     * 获取用户访问数量
     */
    public long accessCount();
}
