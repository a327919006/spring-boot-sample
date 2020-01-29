package com.cn.boot.sample.server.shard;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import tk.mybatis.spring.annotation.MapperScan;

/**
 * 服务层启动类
 *
 * @author Chen Nan
 */
@SpringBootApplication
@MapperScan({"com.cn.boot.sample.dal.mapper"})
public class ServerShardApplication {
    public static void main(String[] args) {
        SpringApplication.run(ServerShardApplication.class, args);
    }
}
