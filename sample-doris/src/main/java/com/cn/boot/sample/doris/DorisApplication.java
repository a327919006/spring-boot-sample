package com.cn.boot.sample.doris;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author Chen Nan
 */
@SpringBootApplication
@MapperScan({
        "com.cn.boot.sample.doris.mapper"})
public class DorisApplication {
    public static void main(String[] args) {
        SpringApplication.run(DorisApplication.class, args);
    }

}
