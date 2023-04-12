package com.example.know.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * 用户权限表
 *
 * @author bookWorm
 */
@Data
@TableName("users_role")
public class UsersRole {

    @TableField("uid")
    private int uid;

    @TableField("rid")
    private int rid;
}
