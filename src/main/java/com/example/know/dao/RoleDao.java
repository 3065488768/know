package com.example.know.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.know.entity.Role;
import org.apache.ibatis.annotations.Mapper;

/**
 * 职权
 *
 * @author bookWorm
 */
@Mapper
public interface RoleDao extends BaseMapper<Role> {
}
