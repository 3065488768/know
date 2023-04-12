package com.example.know.dao;

/**
 * 定时任务
 *
 * @author bookWorm
 */
public interface ScheduleTaskDao {
    /**
     * 将所有的帖子进行热度计算并赋值
     */
    public void calculateHeat();
}
