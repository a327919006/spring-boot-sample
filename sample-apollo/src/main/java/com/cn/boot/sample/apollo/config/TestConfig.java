package com.cn.boot.sample.apollo.config;

import com.cn.boot.sample.apollo.config.properties.Test2ConfigProperties;
import com.ctrip.framework.apollo.Config;
import com.ctrip.framework.apollo.model.ConfigChangeEvent;
import com.ctrip.framework.apollo.spring.annotation.ApolloConfig;
import com.ctrip.framework.apollo.spring.annotation.ApolloConfigChangeListener;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

/**
 * @author Chen Nan
 */
@Slf4j
@Configuration
public class TestConfig {
    @ApolloConfig
    private Config config;
    @ApolloConfig("test2.yaml")
    private Config test2Config;

    @Autowired
    private Test2ConfigProperties test2Properties;

    @ApolloConfigChangeListener
    private void normalChange(ConfigChangeEvent changeEvent) {
        log.info("【someOnChange】namespace={}", changeEvent.getNamespace());

        String key = "test.config.name";
        if (changeEvent.isChanged(key)) {
            log.info("【someOnChange】key={}, value={}", key, config.getProperty(key, ""));
        }
    }

    @ApolloConfigChangeListener(value = "test2.yaml", interestedKeyPrefixes = Constant.PREFIX)
    private void test2Change(ConfigChangeEvent changeEvent) {
        log.info("【test2Change】namespace={},keys={}", changeEvent.getNamespace(), changeEvent.changedKeys());
        changeEvent.changedKeys().forEach(key -> {
            String temp = StringUtils.remove(key, "[");
            temp = StringUtils.remove(temp, "]");
            temp = StringUtils.remove(temp, Constant.PREFIX);
            String[] strings = StringUtils.split(temp, ".");
            int num = Integer.parseInt(strings[0]);
            String field = strings[1];
            String value = test2Config.getProperty(key, "");
            log.info("【test2Change】num={}, field={}, value={}", num, field, value);

            test2Properties.getUsers().get(num).setName(value);
        });
    }
}
