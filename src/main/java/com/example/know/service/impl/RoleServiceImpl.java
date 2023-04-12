package com.example.know.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.example.know.dao.RoleDao;
import com.example.know.dao.UserRoleDao;
import com.example.know.entity.Role;
import com.example.know.entity.UsersRole;
import com.example.know.service.RoleService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Objects;

@Service
public class RoleServiceImpl implements RoleService {

    @Resource
    private RoleDao roleDao;

    @Resource
    private UserRoleDao userRoleDao;

    @Override
    public List<Role> getAllRole() {
        return roleDao.selectList(null);
    }

    @Override
    public int updateRole(Role role) {
        Role role1 = roleDao.selectById(role.getRid());
        role1.setRoleName(role.getRoleName());
        role1.setRoleDesc(role.getRoleDesc());
        return roleDao.updateById(role1);
    }

    @Override
    public int deleteRole(int roleId) {
        LambdaQueryWrapper<UsersRole> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(UsersRole::getRid, roleId);
//        没有用户正在使用此职位
        if (userRoleDao.selectCount(lambdaQueryWrapper) == 0) {
            if (Objects.isNull(roleDao.selectById(roleId))) {
                return 0;
            } else {
                return 1;
            }
        }
        return -1;
    }

    @Override
    public int addRole(Role role) {
        return roleDao.insert(role);
    }
}
