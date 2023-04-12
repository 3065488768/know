package com.example.know.service;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.know.dao.PermissionDao;
import com.example.know.entity.Permission;

import java.util.List;

public interface PermissionService {

    public List<Permission> getPermissionList();
}
