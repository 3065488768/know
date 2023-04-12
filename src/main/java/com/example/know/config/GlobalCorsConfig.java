package com.example.know.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

/**
 * 跨域处理配置
 *
 * @author bookWorm
 */
@Configuration
public class GlobalCorsConfig {
    @Bean
    public CorsFilter corsFilter() {
        CorsConfiguration corsConfiguration = new CorsConfiguration();
//        允许的域
//        corsConfiguration.addAllowedOrigin("http://localhost:8080");
        corsConfiguration.addAllowedOriginPattern("*");
//        是否发送cookie
//        corsConfiguration.setAllowCredentials(true);
//        允许的请求
        corsConfiguration.addAllowedMethod("OPTIONS");
        corsConfiguration.addAllowedMethod("GET");
        corsConfiguration.addAllowedMethod("HEAD");
        corsConfiguration.addAllowedMethod("PUT");
        corsConfiguration.addAllowedMethod("POST");
        corsConfiguration.addAllowedMethod("DELETE");
        corsConfiguration.addAllowedMethod("PATCH");
//        允许头信息
        corsConfiguration.addAllowedHeader("*");
//        添加映射路径，连接一切请求
        UrlBasedCorsConfigurationSource configurationSource = new UrlBasedCorsConfigurationSource();
        configurationSource.registerCorsConfiguration("/**", corsConfiguration);
//        返回新的CorsFilter
        return new CorsFilter(configurationSource);
    }
}
