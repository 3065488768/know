package com.example.know.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.know.entity.User;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * 数据交互层
 *
 * @author bookWorm
 */
@Mapper
public interface UserDao extends BaseMapper<User> {

    /**
     * 删除单个用户
     *
     * @param userId 用户id
     * @return 成功数量
     */
    public int deleteUser(int userId);

    /**
     * 批量删除用户
     *
     * @param userIds 多个用户id
     * @return 成功数量
     */
    public int deleteUsers(int[] userIds);

    /**
     * 用id查询用户
     *
     * @param userId 用户id
     * @return 用户大部分信息
     */
    public User selectOfUserId(int userId);

    /**
     * 用userName查询用户
     *
     * @param userName 用户名
     * @return 用户大部分信息
     */
    public User selectOfUserName(String userName);

    /**
     * 用userName查询用户
     *
     * @param userName 用户名
     * @return 用户少部分信息
     */
    public User checkOfUserNameUnique(String userName);

    /**
     * 用id查询用户
     *
     * @param nickName 用户昵称
     * @return 用户少部分信息
     */
    public User checkOfNickNameUnique(String nickName);

    /**
     * 用id查询用户
     *
     * @param email 用户邮箱
     * @return 用户少部分信息
     */
    public User checkOfEmailUnique(String email);

    /**
     * 论坛中高能用户排行
     *
     * @return
     */
    public List userOfRanking();
}
