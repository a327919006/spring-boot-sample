package com.cn.boot.sample.server.mp.config.mybatis;

import cn.hutool.core.util.IdUtil;
import com.baomidou.mybatisplus.core.incrementer.IKeyGenerator;
import com.baomidou.mybatisplus.core.incrementer.IdentifierGenerator;
import com.baomidou.mybatisplus.extension.incrementer.H2KeyGenerator;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

/**
 * 自定义ID生成器，默认MyBatisPlus的DefaultIdentifierGenerator
 * 雪花ID在容器化环境由于主机名和PID相同，会生成相同ID
 *
 * @author Chen Nan
 */
@Component
public class CustomIdGenerator implements IdentifierGenerator {

    /**
     * 当MyBatisPlus生成ID时，自动会调用自定义ID生成器
     *
     * @param entity 当前需要创建ID的对象，entity中可取到类名及entity所属对象目前设置的所有属性
     * @return 分布式ID
     */
    @Override
    public Long nextId(Object entity) {
        // 可根据entity信息针对不同业务创建不同ID
        // String bizKey = entity.getClass().getName();
        // 根据业务键调用分布式ID生成服务，返回生成的ID值
        return IdUtil.getSnowflakeNextId();
    }

    @Bean
    public IKeyGenerator keyGenerator() {
        return new H2KeyGenerator();
    }
}
