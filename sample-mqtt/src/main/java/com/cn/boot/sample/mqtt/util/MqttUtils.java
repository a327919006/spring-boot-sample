package com.cn.boot.sample.mqtt.util;

import com.cn.boot.sample.api.model.dto.RspBase;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CountDownLatch;

/**
 * @author Chen Nan
 */
public class MqttUtils {
    private static final ConcurrentHashMap<String, CountDownLatch> lockMap = new ConcurrentHashMap<>();
    private static final ConcurrentHashMap<String, RspBase<Object>> resultMap = new ConcurrentHashMap<>();

    public static void lock(String id, CountDownLatch countDownLatch) {
        lockMap.put(id, countDownLatch);
    }

    public static void unlock(String id, RspBase<Object> result) {
        CountDownLatch countDownLatch = lockMap.get(id);
        if (countDownLatch != null) {
            resultMap.put(id, result);
            countDownLatch.countDown();
            lockMap.remove(id);
        }
    }

    public static RspBase<Object> getResult(String id) {
        return resultMap.remove(id);
    }
}
