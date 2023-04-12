package com.example.know.security.other;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.ArrayList;
import java.util.List;

/**
 * 配置放行路径
 *
 * @author bookWorm
 */
@Getter
@Setter
public class IgnoreUrlsConfig {

    private List<String> urls = new ArrayList<>();

    public IgnoreUrlsConfig() {

        this.urls = urls;
    }
}
