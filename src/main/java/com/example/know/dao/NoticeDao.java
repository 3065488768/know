package com.example.know.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.know.entity.Notice;
import org.apache.ibatis.annotations.Mapper;

/**
 * 通知
 *
 * @author bookWorm
 */
@Mapper
public interface NoticeDao extends BaseMapper<Notice> {
}
