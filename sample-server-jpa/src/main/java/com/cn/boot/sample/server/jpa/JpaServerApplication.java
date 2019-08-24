package com.cn.boot.sample.server.jpa;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;

/**
 * 如果Repository不在此模块扫描范围，需配置此注解
 * EnableJpaRepositories(basePackages = "xxx.xxx.xxx")
 *
 * @author Chen Nan
 */
@SpringBootApplication
@EntityScan("com.cn.boot.sample.api.model.po")
public class JpaServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(JpaServerApplication.class, args);
    }
}
