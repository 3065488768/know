package com.example.know.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * 书籍路径表
 *
 * @author bookWorm
 */
@Data
@TableName("book_path")
public class BookPath {

    @TableField("book_id")
    private int bookId;

    @TableField("status")
    private char status;

    @TableField("path")
    private String path;
}
