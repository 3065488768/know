package com.example.know.listener;

import com.example.know.dao.SystemParameterDao;
import com.example.know.enumeration.Setting;
import com.example.know.util.RedisConnectUtil;
import lombok.SneakyThrows;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.event.ApplicationEnvironmentPreparedEvent;
import org.springframework.boot.context.event.ApplicationPreparedEvent;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.ContextClosedEvent;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.ContextStartedEvent;
import org.springframework.context.event.ContextStoppedEvent;

import javax.annotation.Resource;

/**
 * springboot 生命周期
 *
 * @author bookworm
 */
@Configuration
public class ApplicationEventListener implements ApplicationListener<ApplicationEvent> {

    private Logger logger = LoggerFactory.getLogger(getClass());

    @Resource
    private SystemParameterDao systemParameterDao;

    @SneakyThrows
    @Override
    public void onApplicationEvent(ApplicationEvent event) {
        // 在这里可以监听到Spring Boot的生命周期
        if (event instanceof ApplicationEnvironmentPreparedEvent) { // 初始化环境变量
            logger.info("初始化环境变量");
        } else if (event instanceof ApplicationPreparedEvent) { // 初始化完成
            logger.info("初始化环境变量完成");
        } else if (event instanceof ContextStartedEvent) { // 应用启动，Spring2.5新增的事件，当容器调用ConfigurableApplicationContext的 Start()方法开始/重新开始容器时触发该事件。
            logger.info("应用启动");
        } else if (event instanceof ContextRefreshedEvent) { // 应用刷新，当ApplicationContext初始化或者刷新时触发该事件。
            logger.info("应用刷新");
        } else if (event instanceof ApplicationReadyEvent) {// 应用已启动完成
            if (RedisConnectUtil.redisIsConnect()) {
                logger.info("个性配置");
                logger.info("redis连接成功");
                Setting.SHOWNUMBER = systemParameterDao.selectById(1).getNumber();
                logger.info("分页数量配置:{}", Setting.SHOWNUMBER);
            } else {
                logger.error("============redis连接失败===============");
                throw new Exception("redis未开启，请开启后重启项目");
            }
            logger.info("应用已启动完成");
        } else if (event instanceof ContextStoppedEvent) { // 应用停止，Spring2.5新增的事件，当容器调用ConfigurableApplicationContext 的Stop()方法停止容器时触发该事件。
            logger.info("应用停止");
        } else if (event instanceof ContextClosedEvent) { // 应用关闭，当ApplicationContext被关闭时触发该事件。容器被关闭时，其管理的所有 单例Bean都被销毁。
            logger.info("应用关闭");
        } else {
        }
    }

}

