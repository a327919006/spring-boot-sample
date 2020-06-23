package com.cn.boot.sample.guava.eventbus;

import com.google.common.eventbus.Subscribe;
import lombok.extern.slf4j.Slf4j;

/**
 * @author Chen Nan
 */
@Slf4j
public class MultiListener extends BaseListener {

    @Subscribe
    public void doAction1(String event) {
        log.info("doAction1 event={}", event);
    }

    @Subscribe
    public void doAction2(String event) {
        log.info("doAction2 event={}", event);
    }

    @Subscribe
    public void doAction3(Integer event) {
        log.info("doAction3 event={}", event);
    }
}
