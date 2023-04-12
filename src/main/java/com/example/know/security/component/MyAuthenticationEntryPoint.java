package com.example.know.security.component;

import cn.hutool.extra.tokenizer.Result;
import cn.hutool.json.JSONUtil;
import com.example.know.util.AjaxResult;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 身份验证入口
 *
 * @author bookWorm
 */
public class MyAuthenticationEntryPoint implements AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response,
                         AuthenticationException authException) throws IOException, ServletException {
        response.setHeader("Access-Control-Allow-Origin", "*");
        response.setHeader("Cache-Control", "no-cache");
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json");
        response.getWriter().println(JSONUtil.parse(new AjaxResult(AjaxResult.Type.USER_NOT_LOGIN, "因此内容需要您的信息认证，请登陆重试")));
        response.getWriter().flush();
    }
}