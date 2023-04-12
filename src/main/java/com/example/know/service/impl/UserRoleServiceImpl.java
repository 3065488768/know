package com.example.know.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.example.know.dao.UserRoleDao;
import com.example.know.entity.UsersRole;
import com.example.know.service.UserRoleService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

@Service
public class UserRoleServiceImpl implements UserRoleService {

    @Resource
    private UserRoleDao userRoleDao;

    @Override
    public List<Map> getAllUserRole() {
        return userRoleDao.getAllUsersRole();
    }

    @Override
    public boolean bindUserRole(int userId, int roleId) {
        LambdaQueryWrapper<UsersRole> lambdaQueryWrapper = new LambdaQueryWrapper();
        lambdaQueryWrapper.eq(UsersRole::getUid, userId);
        lambdaQueryWrapper.eq(UsersRole::getRid, roleId);
        List<UsersRole> usersRoleList = userRoleDao.selectList(lambdaQueryWrapper);
        if (usersRoleList.size() == 0) {
            UsersRole usersRole = new UsersRole();
            usersRole.setUid(userId);
            usersRole.setRid(roleId);
            int insert = userRoleDao.insert(usersRole);
            if (insert == 1) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean removeUserRoleBind(int userId, int roleId) {
        LambdaQueryWrapper<UsersRole> lambdaQueryWrapper = new LambdaQueryWrapper();
        lambdaQueryWrapper.eq(UsersRole::getUid, userId);
        lambdaQueryWrapper.eq(UsersRole::getRid, roleId);
        List<UsersRole> usersRoleList = userRoleDao.selectList(lambdaQueryWrapper);
        if (usersRoleList.size() == 1) {
            int delete = userRoleDao.delete(lambdaQueryWrapper);
            if (delete == 1) {
                return true;
            }
        }
        return false;
    }
}
