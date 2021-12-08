package com.cn.boot.sample.cadence.service.impl;

import com.cn.boot.sample.cadence.service.IActivityService;
import lombok.extern.slf4j.Slf4j;

/**
 * @author Chen Nan
 */
@Slf4j
public class ActivityServiceImpl implements IActivityService {
    @Override
    public String doHello(String name) {
        log.info("doHello name={}", name);
        return "hello " + name;
    }
}
