package com.example.know.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.know.entity.BookType;
import com.example.know.entity.Digest;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

/**
 * 书摘
 *
 * @author bookWorm
 */
@Mapper
public interface DigestDao extends BaseMapper<Digest> {

    /**
     * 获取书摘
     *
     * @return
     */
    public List getDigestAndBook();
}
