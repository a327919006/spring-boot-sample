package com.cn.boot.sample.websocket.client.service.impl;

import com.cn.boot.sample.websocket.client.service.MsgService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.concurrent.atomic.AtomicLong;

/**
 * @author Chen Nan
 */
@Slf4j
@Service
public class MsgServiceImpl implements MsgService {
    private AtomicLong count = new AtomicLong(0);

    @Override
    public void process(String message) {
        log.info("收到消息: {}", message);
        String[] split = message.split("\n");
        log.info("count={}", count.addAndGet(split.length));
    }
}
