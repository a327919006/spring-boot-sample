package com.cn.boot.sample.iotdb;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author Chen Nan
 */
@SpringBootApplication
@MapperScan({
        "com.cn.boot.sample.iotdb.mapper"})
public class IotDbApplication {
    public static void main(String[] args) {
        SpringApplication.run(IotDbApplication.class, args);
    }

}
