package com.example.know.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class CommentAndAuthor {

    private int commentId;

    private String commentableId;

    private char commentableType;

    private String content;

    private LocalDateTime createTime;

    private User user;
}
