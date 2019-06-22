package com.cn.boot.sample.server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
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
@ComponentScan(basePackages = {
        "com.cn.boot.sample.dal.mapper",
        "com.cn.boot.sample.server.config"
})
public class ServerApplication {
    public static void main(String[] args) {
        SpringApplication.run(ServerApplication.class, args);
    }
}
