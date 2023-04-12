package com.example.know.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.Date;

/**
 * 职权
 *
 * @author bookWorm
 */
@Data
@TableName("role")
public class Role {

    @TableId(value = "rid")
    private int rid;

    @TableField(value = "role_name")
    private String roleName;

    @TableField(value = "role_desc")
    private String roleDesc;

    @TableField(value = "create_time")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;
}
