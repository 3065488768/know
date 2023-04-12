package com.example.know.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.know.entity.Transactions;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface TransactionsDao extends BaseMapper<Transactions> {
}
