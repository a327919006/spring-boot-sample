package com.cn.boot.sample.graphql.config;

import graphql.execution.instrumentation.InstrumentationState;
import lombok.extern.slf4j.Slf4j;

/**
 * 在整个执行过程中都会传递这个State对象
 *
 * @author Chen Nan
 */
@Slf4j
public class CustomInstrumentationState implements InstrumentationState {
    private long start = System.currentTimeMillis();

    public void cost() {
//        log.info("cost={}ms", System.currentTimeMillis() - start);
    }
}

