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
 * 收藏
 *
 * @author bookWorm
 */
@Data
@TableName("collection")
public class Collection {
    @TableId("collection_id")
    private int collectionId;

    @TableField("collected_id")
    private int collectedId;

    @TableField("collection_type")
    private char collectionType;

    @TableField("collection_by")
    private int collectionBy;

    @TableField(value = "create_time", fill = FieldFill.INSERT)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;
}
