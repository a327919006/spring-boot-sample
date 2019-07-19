package com.cn.boot.sample.server.druid.config.uid;

import com.baidu.fsg.uid.impl.DefaultUidGenerator;
import com.baidu.fsg.uid.worker.DisposableWorkerIdAssigner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author Chen Nan
 */
@Configuration
public class UidGenerator {

    /**
     * 生成id分配器
     */
    @Bean
    public DisposableWorkerIdAssigner assigner() {
        return new DisposableWorkerIdAssigner();
    }

    @Bean
    public DefaultUidGenerator generator(DisposableWorkerIdAssigner assigner) {
        DefaultUidGenerator generator = new DefaultUidGenerator();
        generator.setWorkerIdAssigner(assigner);
        generator.setTimeBits(29);
        generator.setWorkerBits(21);
        generator.setSeqBits(13);
        generator.setEpochStr("2016-09-20");
        return generator;
    }
}
