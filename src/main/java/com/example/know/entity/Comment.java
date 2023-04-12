package com.example.know.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.Date;

/**
 * @author bookWorm
 */
@Data
@TableName(value = "comment")
public class Comment {
    @TableId(value = "comment_id")
    private int commentId;

    @TableField(value = "commentable_id")
    private int commentableId;

    @TableField(value = "commentable_type")
    private char commentableType;

    @TableField(value = "comment_by")
    private int commentBy;

    @TableField(value = "content")
    private String content;

    @TableField(value = "create_time", fill = FieldFill.INSERT)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;

}
