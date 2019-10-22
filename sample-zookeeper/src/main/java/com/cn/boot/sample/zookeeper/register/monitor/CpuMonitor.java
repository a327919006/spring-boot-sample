package com.cn.boot.sample.zookeeper.register.monitor;

import com.cn.boot.sample.zookeeper.register.constants.HazelcastConstant;
import com.cn.boot.sample.zookeeper.register.properties.ServerConfig;
import com.cn.boot.sample.zookeeper.register.properties.ServerInfo;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.IMap;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import oshi.SystemInfo;
import oshi.hardware.CentralProcessor;
import oshi.hardware.HardwareAbstractionLayer;
import oshi.util.Util;

import javax.annotation.PostConstruct;
import java.util.Timer;
import java.util.TimerTask;

/**
 * @author Chen Nan
 */
@Slf4j
@Component
public class CpuMonitor {
    /**
     * 定时任务启动延时
     */
    private int delay = 0;
    /**
     * 定时任务执行间隔
     */
    private int period = 60000;
    /**
     * 计算CPU睡眠时间
     */
    private int sleep = 20000;
    /**
     * 当前服务节点CPU
     */
    private static double cpuRate = 0;

    @Autowired
    private ServerConfig serverConfig;
    @Autowired
    private HazelcastInstance hazelcastInstance;

    @PostConstruct
    public void init() {
        new Timer("CpuMonitor").schedule(new TimerTask() {
            @Override
            public void run() {
                SystemInfo si = new SystemInfo();
                HardwareAbstractionLayer hal = si.getHardware();
                CentralProcessor processor = hal.getProcessor();

                long[] prevTicks = processor.getSystemCpuLoadTicks();
                // 计算CPU需睡眠一段时间，至少1秒
                Util.sleep(sleep);
                cpuRate = processor.getSystemCpuLoadBetweenTicks(prevTicks) * 100;
                log.info(String.format("【CpuMonitor】CPU load: %.1f%%", cpuRate));

                // 数据更新至缓存
                hazelcastInstance.getMap(HazelcastConstant.MAP_SERVER_CPU_RATE).put(serverConfig.getNodeName(), cpuRate);
            }
        }, delay, period);
    }

    /**
     * 获取节点CPU使用率
     *
     * @param nodeName 节点名称
     * @return cpu使用率
     */
    public double getCpuRate(String nodeName) {
        IMap<String, Double> map = hazelcastInstance.getMap(HazelcastConstant.MAP_SERVER_CPU_RATE);
        Double cpuRate = map.get(nodeName);
        return cpuRate == null ? 0 : cpuRate;
    }

    /**
     * 判断当前节点CPU是否到达上限
     *
     * @return 是否到达上限 true
     */
    public boolean overLimit() {
        String nodeName = serverConfig.getNodeName();
        ServerInfo serverInfo = serverConfig.getServers().get(nodeName);
        if (cpuRate >= serverInfo.getMaxCpu()) {
            return true;
        }
        return false;
    }
}
