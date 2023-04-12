package com.example.know.config;

import com.example.know.security.DynamicAccessDecisionManager;
import com.example.know.security.DynamicSecurityFilter;
import com.example.know.security.component.MyAccessDeniedHandler;
import com.example.know.security.component.MyAuthenticationEntryPoint;
import com.example.know.security.filter.JwtAuthenticationTokenFilter;
import com.example.know.security.other.DynamicSecurityMetadataSource;
import com.example.know.security.other.IgnoreUrlsConfig;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.AccessDecisionManager;
import org.springframework.security.access.AccessDecisionVoter;
import org.springframework.security.access.vote.UnanimousBased;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.FilterInvocation;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.expression.WebExpressionVoter;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.util.Arrays;
import java.util.List;


/**
 * 验证配置类
 *
 * @author bookWorm
 */
@Configuration
@EnableWebSecurity(debug = true)
public class SecurityConfig {

    /**
     * 加密方式
     *
     * @return
     */
    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public IgnoreUrlsConfig ignoreUrlsConfig() {
        return new IgnoreUrlsConfig();
    }

    @Bean
    public MyAccessDeniedHandler myAccessDeniedHandler() {
        return new MyAccessDeniedHandler();
    }

    @Bean
    public MyAuthenticationEntryPoint myAuthenticationEntryPoint() {
        return new MyAuthenticationEntryPoint();
    }

    @Bean
    public JwtAuthenticationTokenFilter jwtAuthenticationTokenFilter() {
        return new JwtAuthenticationTokenFilter();
    }

    @ConditionalOnBean(name = "dynamicSecurityService")
    @Bean
    public DynamicAccessDecisionManager dynamicAccessDecisionManager() {
        return new DynamicAccessDecisionManager();
    }

    @ConditionalOnBean(name = "dynamicSecurityService")
    @Bean
    public DynamicSecurityMetadataSource dynamicSecurityMetadataSource() {
        return new DynamicSecurityMetadataSource();
    }

    @ConditionalOnBean(name = "dynamicSecurityService")
    @Bean
    public DynamicSecurityFilter dynamicSecurityFilter() {
        return new DynamicSecurityFilter();
    }

    /**
     * 直接在过滤器链里面配置httpSecurity
     *
     * @param http
     * @return
     * @throws Exception
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                //关闭csrf
                .csrf().disable()
                //不通过Session获取SecurityContext
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                // 允许跨域
                .cors()
                .and()
                // 配置路劲是否需要认证
                .authorizeRequests()
                // 对于登录接口 允许匿名访问
                .antMatchers("/index/**", "/book/**", "/verification/**", "/forum/**", "/test/**", "/websocket/*").permitAll()
                .antMatchers("/user/login").permitAll()

                // 配置权限
//                .antMatchers("/admin").hasAuthority("/hello2")
                // 除上面外的所有请求全部需要鉴权认证
                .anyRequest().authenticated()
                .and()
                // 添加未登录与权限不足异常处理器
                .exceptionHandling()
                .accessDeniedHandler(myAccessDeniedHandler())
                .authenticationEntryPoint(myAuthenticationEntryPoint())
                .and()
                .addFilterBefore(jwtAuthenticationTokenFilter(), UsernamePasswordAuthenticationFilter.class)
                .formLogin()
                .and()
                .build();
    }


}


