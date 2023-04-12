package com.example.know.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Date;

/**
 * 用户表
 *
 * @author bookWorm
 */
@Data
@TableName("user")
public class User {

    /**
     * @value 用户固有id
     */
    @TableId(value = "user_id")
    private int userId;

    /**
     * @value 用户名
     */
    @TableField(value = "user_name")
    private String userName;

    /**
     * @value 昵称
     */
    @TableField(value = "nick_name")
    private String nickName;

    /**
     * @value 用户类型 1-用户 2-版主 3-管理
     */
    @TableField(value = "user_type")
    private char userType;

    /**
     * @value 密码
     */
    @TableField(value = "password")
    private String password;

    /**
     * @value 邮箱
     */
    @TableField(value = "email")
    private String email;

    /**
     * @value 性别 0-男 1-女 2-未知
     */
    @TableField(value = "sex")
    private char sex;

    /**
     * 头像路径
     */
    @TableField(value = "avator")
    private String avator;

    /**
     * @value 加密盐
     */
    @TableField(value = "salt")
    private String salt;

    /**
     * @value 状态 0-正常 1-停用 2-删除
     */
    @TableField(value = "status")
    private char status;

    /**
     * @value 书币数量
     */
    @TableField(value = "book_currency")
    private int bookCurrency;

    /**
     * @value 创建ip
     */
    @TableField(value = "create_ip")
    private String createIp;

    /**
     * @value 创建时间
     */
    @TableField(value = "create_time", fill = FieldFill.INSERT)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;

    /**
     * @value 个人简介
     */
    @TableField(value = "remark")
    private String remark;

}
