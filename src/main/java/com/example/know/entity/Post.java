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
@TableName(value = "post")
@Data
public class Post {
    @TableId(value = "post_id")
    private int postId;

    @TableField(value = "post_title")
    private String postTitle;

    @TableField(value = "post_content")
    private String postContent;

    @TableField(value = "plate_id")
    private int plateId;

    @TableField(value = "post_type")
    private char postType;

    @TableField(value = "status")
    private char status;

    @TableField(value = "book_id")
    private int bookId;

    @TableField(value = "heat_degree")
    private int degreeOfHeat;

    @TableField(value = "create_by")
    private int createBy;

    @TableField(value = "create_time", fill = FieldFill.INSERT)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;

    @TableField(value = "update_time", fill = FieldFill.UPDATE)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updateTime;
}
