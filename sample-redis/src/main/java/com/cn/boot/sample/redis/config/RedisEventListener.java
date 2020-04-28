package com.cn.boot.sample.redis.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;

/**
 * key相关操作监听器
 *
 * @author Chen Nan
 */
@Slf4j
public class RedisEventListener implements MessageListener {
    @Override
    public void onMessage(Message message, byte[] bytes) {
        byte[] body = message.getBody();// 建议使用: valueSerializer
        byte[] channel = message.getChannel();
        log.info("onMessage >> ");
        log.info("channel:{}, body:{}, bytes:{}", new String(channel), new String(body), new String(bytes));
    }
}
