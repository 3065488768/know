package com.example.know.dao;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.know.entity.RolePermission;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @author bookWorm
 */
@Mapper
public interface RolePermissionDao extends BaseMapper<RolePermission> {

    public List getRoleAndPermission(int roleId);
}
