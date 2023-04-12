package com.example.know.service;

import com.example.know.dao.RolePermissionDao;
import com.example.know.entity.Permission;
import com.example.know.entity.Role;
import com.example.know.entity.RolePermission;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface RolePermissionService {

    public boolean setRolePermission(int roleId, int[] rolePermissionIds);

    public List getRoleAndPermission(int roleId);
}
