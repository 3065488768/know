package com.example.know.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.Date;

/**
 * 权限表
 *
 * @author bookWorm
 */
@Data
@TableName("permission")
public class Permission {

    @TableId("pid")
    private int pid;

    @TableField(value = "permission_name")
    private String permissionName;

    @TableField(value = "url")
    private String url;

    @TableField(value = "create_time")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;
}
