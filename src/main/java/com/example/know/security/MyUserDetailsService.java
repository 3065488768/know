package com.example.know.security;

import com.example.know.dao.PermissionDao;
import com.example.know.dao.UserDao;
import com.example.know.entity.MyUserDetails;
import com.example.know.entity.Permission;
import com.example.know.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;

/**
 * @author bookWorm
 */
@Component
@RequiredArgsConstructor
public class MyUserDetailsService implements UserDetailsService {

    private final UserDao userDao;

    private final PermissionDao permissionDao;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userDao.selectOfUserName(username);
        if (!Objects.isNull(user)) {
            List<Permission> permissionList = permissionDao.permissionByUserId(user.getUserId());
            return MyUserDetails.builder().user(user).permissionList(permissionList).build();
        }
        throw new UsernameNotFoundException("用户名或密码错误");
    }
}
