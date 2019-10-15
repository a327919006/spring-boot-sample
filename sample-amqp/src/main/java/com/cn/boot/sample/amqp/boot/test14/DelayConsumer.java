package com.cn.boot.sample.amqp.boot.test14;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

/**
 * @author Chen Nan
 */
@Slf4j
//@Component
public class DelayConsumer {

    @RabbitListener(queues = "test14.delay.queue")
    public void onMessage(byte[] data) {
        log.info("-----onMessage------");
        log.info("data = " + new String(data));
    }
}
