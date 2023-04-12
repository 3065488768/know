package com.example.know.entity;

import cn.hutool.core.date.DateUtil;
import com.alibaba.fastjson2.util.DateUtils;
import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.Date;

/**
 * 访问帖子记录
 *
 * @author bookWorm
 */
@Data
@TableName(value = "access")
public class Access {

    @TableId(value = "access_id")
    private int accessId;

    @TableField(value = "access_ip")
    private String accessIp;

    @TableField(value = "post_id")
    private int postId;

    @TableField(value = "access_by")
    private int accessBy;

    @TableField(value = "create_time", fill = FieldFill.INSERT)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;

    public Access(String accessIp, int postId, int accessBy) {
        this.accessIp = accessIp;
        this.postId = postId;
        this.accessBy = accessBy;
    }
}
