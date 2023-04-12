package com.example.know.service;

import java.util.List;
import java.util.Map;

public interface UserRoleService {

    public List<Map> getAllUserRole();

    public boolean bindUserRole(int userId, int roleId);

    public boolean removeUserRoleBind(int userId, int roleId);
}
