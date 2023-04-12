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
 * 私信
 *
 * @author bookWorm
 */
@Data
@TableName("letter")
public class Letter {
    @TableId("letter_id")
    private int letterId;

    @TableField("sender")
    private int sendId;

    @TableField("recipient")
    private int recipientId;

    @TableField("status")
    private char status;

    @TableField("content")
    private String content;

    @TableField(value = "create_time", fill = FieldFill.INSERT)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;
}
