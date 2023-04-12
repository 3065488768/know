package com.example.know.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@TableName("system_parameter")
@Data
public class SystemParameter {

    @TableId("parameter_id")
    private int parameterId;

    @TableField("parameter_type")
    private int parameter_type;

    @TableField("number")
    private int number;

    @TableField("remark")
    private String remark;
}
