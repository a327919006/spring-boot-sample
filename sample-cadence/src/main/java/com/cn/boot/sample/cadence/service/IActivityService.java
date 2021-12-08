package com.cn.boot.sample.cadence.service;

import com.uber.cadence.activity.ActivityMethod;

/**
 * @author Chen Nan
 */
public interface IActivityService {
    @ActivityMethod(scheduleToCloseTimeoutSeconds = 2)
    String doHello(String name);
}
