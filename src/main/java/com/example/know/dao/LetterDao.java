package com.example.know.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.know.entity.Letter;
import org.apache.ibatis.annotations.Mapper;

/**
 * 私信
 *
 * @author bookWorm
 */
@Mapper
public interface LetterDao extends BaseMapper<Letter> {
}
