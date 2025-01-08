package com.cn.boot.sample.server.mp;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * 服务层启动类
 *
 * @author Chen Nan
 */
@SpringBootApplication
@MapperScan({
        "com.cn.boot.sample.dal.mp.mapper",
        "com.baidu.fsg.uid.worker.dao"})
public class ServerMpApplication {
    public static void main(String[] args) {
        SpringApplication.run(ServerMpApplication.class, args);
    }
}
