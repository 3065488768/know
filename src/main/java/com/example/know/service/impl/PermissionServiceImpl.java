package com.example.know.service.impl;

import com.example.know.dao.PermissionDao;
import com.example.know.entity.Permission;
import com.example.know.service.PermissionService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class PermissionServiceImpl implements PermissionService {

    @Resource
    private PermissionDao permissionDao;

    @Override
    public List<Permission> getPermissionList() {
        return permissionDao.selectList(null);
    }
}
