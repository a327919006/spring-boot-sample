package com.cn.boot.sample.business.async;

import cn.hutool.core.thread.ThreadUtil;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;

/**
 * <p>Title:</p>
 * <p>Description:</p>
 *
 * @author Chen Nan
 * @date 2019/6/15.
 */
@Component
@Data
@Slf4j
public class ClientQueue {
    @Autowired
    private ThreadPoolTaskExecutor poolTaskExecutor;
    private String clientId;

    public void setClientId(String clientId) {
        poolTaskExecutor.execute(() -> {
            log.info("开始发送MQ");
            ThreadUtil.sleep(1000);
            this.clientId = clientId;
            log.info("发送MQ消息成功");
        });

    }
}
