package com.example.know.service;

import com.example.know.dao.RoleDao;
import com.example.know.entity.Role;

import java.util.List;

public interface RoleService {

    public List<Role> getAllRole();

    public int updateRole(Role role);

    public int deleteRole(int roleId);

    public int addRole(Role role);
}
