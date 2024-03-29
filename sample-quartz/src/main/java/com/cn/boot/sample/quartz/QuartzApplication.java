package com.cn.boot.sample.quartz;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;
import tk.mybatis.spring.annotation.MapperScan;

/**
 * 整合SpringTask需使用EnableScheduling注解
 *
 * @author Chen Nan
 */
@EnableScheduling
@SpringBootApplication
@MapperScan({"com.cn.boot.sample.dal.mapper"})
public class QuartzApplication {

    public static void main(String[] args) {
        SpringApplication.run(QuartzApplication.class, args);
    }
}
