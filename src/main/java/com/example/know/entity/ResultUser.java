package com.example.know.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.util.Date;

/**
 * 返回的用户信息
 *
 * @author bookWorm
 */
@Data
public class ResultUser {
    /**
     * @value 用户固有id
     */
    @TableId(value = "user_id")
    private int userId;

    /**
     * @value 昵称
     */
    @TableField(value = "nick_name")
    private String nickName;

    /**
     * 头像路径
     */
    @TableField(value = "avator")
    private String avator;


    public ResultUser(User user) {
        this.userId = user.getUserId();
        this.nickName = user.getNickName();
        this.avator = user.getAvator();
    }
}

