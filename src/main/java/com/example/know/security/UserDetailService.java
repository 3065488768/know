package com.example.know.security;

import com.example.know.dao.UserDao;
import com.example.know.entity.User;
import com.example.know.security.utils.JwtProvider;
import com.example.know.util.AjaxResult;
import com.example.know.util.Md5Utils;
import kotlin.RequiresOptIn;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

/**
 * @author bookWorm
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class UserDetailService {

    private final MyUserDetailsService userDetailsService;

    private final PasswordEncoder passwordEncoder;

    private final JwtProvider jwtProvider;

    private final UserDao userDao;

    public AjaxResult login(String username, String password) {
        String token = null;

        User user = userDao.selectOfUserName(username);
        password = Md5Utils.hash(user.getSalt() + password);
        UserDetails userDetails = userDetailsService.loadUserByUsername(username);
        if (!passwordEncoder.matches(password, userDetails.getPassword())) {
            return new AjaxResult(AjaxResult.Type.USER_PASSWORD_ERROR, "密码错误");
        }
        if (!userDetails.isEnabled()) {
            return new AjaxResult(AjaxResult.Type.USER_ACCOUNT_LOCKED, "用户停用");
        }
        try {
            UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                    userDetails, null, userDetails.getAuthorities()
            );
            SecurityContextHolder.getContext().setAuthentication(authenticationToken);
        } catch (AuthenticationException e) {
            log.error("登陆异常");
        }
        token = String.valueOf(jwtProvider.createToken(userDetails));
        return AjaxResult.success(token);
    }

    public AjaxResult refreshToken(String oldToken) {
        return AjaxResult.success(jwtProvider.refreshToken(oldToken));
    }
}
