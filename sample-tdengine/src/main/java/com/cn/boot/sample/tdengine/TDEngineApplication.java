package com.cn.boot.sample.tdengine;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author Chen Nan
 */
@SpringBootApplication
@MapperScan({
        "com.cn.boot.sample.tdengine.mapper"})
public class TDEngineApplication {
    public static void main(String[] args) {
        SpringApplication.run(TDEngineApplication.class, args);
    }

}
