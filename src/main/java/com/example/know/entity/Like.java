package com.example.know.entity;

import cn.hutool.core.date.DateTime;
import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.Date;

/**
 * 喜欢
 *
 * @author bookWorm
 */
@Data
@TableName("like_table")
public class Like {
    @TableId("like_id")
    private int likeId;

    @TableField("liked_id")
    private int likedId;

    @TableField("like_type")
    private char likeType;

    @TableField("like_by")
    private int likeBy;

    @TableField(value = "create_time", fill = FieldFill.INSERT)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;
}
