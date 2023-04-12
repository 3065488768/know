package com.example.know.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.know.entity.UsersRole;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

/**
 * 用户权限
 *
 * @author bookWorm
 */
@Mapper
public interface UserRoleDao extends BaseMapper<UsersRole> {

    public List<Map> getAllUsersRole();

}
