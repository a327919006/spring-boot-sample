package com.cn.boot.sample.es;

import org.dromara.easyes.starter.register.EsMapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author Chen Nan
 */
@SpringBootApplication
@EsMapperScan("com.cn.boot.sample.es.dao")
public class EsApplication {
    public static void main(String[] args) {
        SpringApplication.run(EsApplication.class, args);
    }
}
