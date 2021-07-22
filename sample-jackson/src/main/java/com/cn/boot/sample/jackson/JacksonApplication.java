package com.cn.boot.sample.jackson;

import com.spring4all.swagger.EnableSwagger2Doc;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author Chen Nan
 */
@EnableSwagger2Doc
@SpringBootApplication
public class JacksonApplication {
    public static void main(String[] args) {
        SpringApplication.run(JacksonApplication.class, args);
    }
}
