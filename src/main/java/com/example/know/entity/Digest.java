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
 * 书摘
 *
 * @author bookWorm
 */
@Data
@TableName("digest")
public class Digest {
    @TableId("digest_id")
    private int digestId;

    @TableField("digest_content")
    private String digestContent;

    @TableField("book_id")
    private int bookId;

    @TableField("author_id")
    private int authorId;

    @TableField(value = "create_time", fill = FieldFill.INSERT)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;
}
