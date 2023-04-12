package com.example.know;

import lombok.extern.slf4j.Slf4j;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * @author bookWorm
 */
@EnableAsync
@EnableScheduling
@Slf4j
@SpringBootApplication
@MapperScan("com.example.know.dao")
@EnableTransactionManagement
public class KnowApplication {
    public static void main(String[] args) {
        SpringApplication.run(KnowApplication.class, args);
    }
}
