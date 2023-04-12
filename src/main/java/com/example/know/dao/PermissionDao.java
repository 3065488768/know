package com.example.know.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.know.entity.Permission;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * 权限
 *
 * @author bookWorm
 */
@Mapper
public interface PermissionDao extends BaseMapper<Permission> {

    /**
     * 根据用户id获取权限路径
     *
     * @param userId 用户id
     * @return
     */
    public List permissionByUserId(int userId);

}
