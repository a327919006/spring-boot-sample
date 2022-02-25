package com.cn.boot.sample.quartz.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * @author Chen Nan
 */
@Slf4j
@Service
public class BusinessService {

    public void doJob(String business) {
        log.info("doJob:{}", business);
    }
}
