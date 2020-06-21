package com.cn.boot.sample.guava.eventbus;

import com.google.common.eventbus.Subscribe;
import lombok.extern.slf4j.Slf4j;

/**
 * @author Chen Nan
 */
@Slf4j
public class SimpleListener {

    @Subscribe
    public void doAction(String event) {
        log.info("doAction event={}", event);
    }
}
