package com.cn.boot.sample.server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import tk.mybatis.spring.annotation.MapperScan;

/**
 * 服务层启动类
 *
 * @author Chen Nan
 */
@SpringBootApplication
@MapperScan({
        "com.cn.boot.sample.dal.mapper",
        "com.baidu.fsg.uid.worker.dao"})
public class ServerApplication {
    public static void main(String[] args) {
        SpringApplication.run(ServerApplication.class, args);
    }
}
