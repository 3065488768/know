package com.example.know.security.component;

import cn.hutool.json.JSONUtil;
import com.example.know.util.AjaxResult;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 访问拒绝
 *
 * @author bookWorm
 */
public class MyAccessDeniedHandler implements AccessDeniedHandler {

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response,
                       AccessDeniedException accessDeniedException) throws IOException, ServletException {
        response.setHeader("Access-Control-Allow-Origin", "*");
        response.setHeader("Cache-Control", "no-cache");
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json");
        response.getWriter()
                .println(JSONUtil.parse(new AjaxResult(AjaxResult.Type.NO_PERMISSION, accessDeniedException.getMessage())));
        response.getWriter().flush();
    }
}
