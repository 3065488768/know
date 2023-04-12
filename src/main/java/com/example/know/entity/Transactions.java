package com.example.know.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName(value = "transactions")
public class Transactions {
    @TableId(value = "transaction_id")
    private int transactionId;

    @TableField(value = "payer_id")
    private int payerId;

    @TableField(value = "payee_id")
    private int payeeId;

    @TableField(value = "transactions_number")
    private int transactionsNumber;

    @TableField(value = "transactions_type")
    private char transactionsType;

    @TableField(value = "create_time", fill = FieldFill.INSERT)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;
}
