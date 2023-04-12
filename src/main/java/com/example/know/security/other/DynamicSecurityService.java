package com.example.know.security.other;

import com.example.know.dao.PermissionDao;
import com.example.know.entity.Permission;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.access.SecurityConfig;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class DynamicSecurityService {

    @Autowired
    private PermissionDao permissionDao;

    // 加载资源ANT通配符和资源对应MAP
    public Map<String, ConfigAttribute> loadDataSource() {
        Map<String, ConfigAttribute> urlAndResourceNameMap = new ConcurrentHashMap<>();
        List<Permission> permissions = permissionDao.selectList(null);
        permissions.forEach(permission -> urlAndResourceNameMap
                .put(permission.getUrl(), new SecurityConfig(permission.getPermissionName())));
        return urlAndResourceNameMap;
    }
}
