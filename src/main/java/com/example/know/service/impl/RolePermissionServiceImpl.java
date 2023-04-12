package com.example.know.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.example.know.dao.RoleDao;
import com.example.know.dao.RolePermissionDao;
import com.example.know.entity.Permission;
import com.example.know.entity.Role;
import com.example.know.entity.RolePermission;
import com.example.know.service.RolePermissionService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class RolePermissionServiceImpl implements RolePermissionService {

    @Resource
    private RoleDao roleDao;

    @Resource
    private RolePermissionDao rolePermissionDao;

    @Override
    public boolean setRolePermission(int roleId, int[] permissionIds) {
        LambdaQueryWrapper<RolePermission> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(RolePermission::getRid, roleId);
        List<RolePermission> rolePermissions = rolePermissionDao.selectList(lambdaQueryWrapper);
        for (int permissionId : permissionIds) {
//            不存在
            if (!rolePermissionExistence(permissionId, rolePermissions)) {
                RolePermission rolePermission = new RolePermission();
                rolePermission.setRid(roleId);
                rolePermission.setPid(permissionId);
                rolePermissionDao.insert(rolePermission);
            }
        }
        return true;
    }

    @Override
    public List getRoleAndPermission(int roleId) {
        return rolePermissionDao.getRoleAndPermission(roleId);
    }

    private boolean rolePermissionExistence(int permissionId, List<RolePermission> permissionIds) {
        for (RolePermission id : permissionIds) {
            if (permissionId == id.getPid()) {
                return true;
            }
        }
        return false;
    }
}
