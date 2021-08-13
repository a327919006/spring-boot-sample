package com.cn.boot.sample.business.version;

import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.autoconfigure.web.servlet.WebMvcRegistrations;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author Chen Nan
 */
@Configuration
@ConditionalOnWebApplication
public class VersionMappingAutoConfiguration {

    @Bean
    public WebMvcRegistrations bladeWebMvcRegistrations() {
        return new SampleWebMvcRegistrations();
    }
}