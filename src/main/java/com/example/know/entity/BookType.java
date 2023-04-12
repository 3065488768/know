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
 * 书籍类别
 *
 * @author bookWorm
 */
@Data
@TableName("book_type")
public class BookType {
    @TableId("book_type_id")
    private int bookTypeId;

    @TableField("book_type_name")
    private String bookTypeName;

    @TableField("plate_id")
    private int plateId;

    @TableField(value = "create_time", fill = FieldFill.INSERT)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;
}
