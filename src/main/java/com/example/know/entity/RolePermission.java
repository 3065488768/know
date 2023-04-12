package com.example.know.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * 职位权限对应表
 *
 * @author bookWorm
 */
@Data
@TableName("role_permission")
public class RolePermission {

    @TableField(value = "rid")
    private int rid;

    @TableField(value = "pid")
    private int pid;
}
