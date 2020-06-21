package com.cn.boot.sample.guava.eventbus;

import com.google.common.eventbus.Subscribe;
import lombok.extern.slf4j.Slf4j;

/**
 * @author Chen Nan
 */
@Slf4j
public class BaseListener {

    @Subscribe
    public void baseAction(String event) {
        log.info("baseAction event={}", event);
    }
}
