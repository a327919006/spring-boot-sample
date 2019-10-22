package com.cn.boot.sample.zookeeper.register.monitor;

import cn.hutool.core.io.IoUtil;
import com.cn.boot.sample.api.util.OsUtil;
import com.cn.boot.sample.zookeeper.register.constants.HazelcastConstant;
import com.cn.boot.sample.zookeeper.register.properties.ServerConfig;
import com.cn.boot.sample.zookeeper.register.properties.ServerInfo;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.IMap;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.nio.charset.StandardCharsets;
import java.util.Timer;
import java.util.TimerTask;

/**
 * @author Chen Nan
 */
@Slf4j
@Component
public class ConnectMonitor {
    /**
     * 定时任务启动延时
     */
    private int delay = 0;
    /**
     * 定时任务执行间隔
     */
    private int period = 60000;
    /**
     * 当前服务节点客户端连接数
     */
    private static int connectCount = 0;

    @Autowired
    private ServerConfig serverConfig;
    @Autowired
    private HazelcastInstance hazelcastInstance;

    @PostConstruct
    public void init() {
        new Timer("ConnectionMonitor").schedule(new TimerTask() {
            @Override
            public void run() {
                // 获取服务端口连接数
                int port = serverConfig.getServerPort();
                String command;
                String charsets;
                if (OsUtil.isWindows()) {
                    command = "cmd.exe /c netstat -an|find \"" + port + "\" /c";
                    charsets = "GBK";
                } else {
                    command = "netstat -nat | grep -iw " + port + " | wc -l";
                    charsets = StandardCharsets.UTF_8.name();
                }

                Runtime runtime = Runtime.getRuntime();
                try {
                    // 获取连接数
                    String result = IoUtil.read(runtime.exec(command).getInputStream(), charsets).trim();
                    connectCount = Integer.parseInt(result);
                    log.info("【ConnectMonitor】当前连接数:{}", result);

                    // 数据更新至缓存
                    hazelcastInstance.getMap(HazelcastConstant.MAP_SERVER_CONNECT_COUNT).put(serverConfig.getNodeName(), connectCount);
                } catch (Exception e) {
                    log.info("【ConnectMonitor】异常:", e);
                }
            }
        }, delay, period);
    }

    /**
     * 获取节点服务端口连接数
     *
     * @param nodeName 节点名称
     * @return 服务端口连接数
     */
    public int getConnectCount(String nodeName) {
        IMap<String, Integer> map = hazelcastInstance.getMap(HazelcastConstant.MAP_SERVER_CONNECT_COUNT);
        Integer count = map.get(nodeName);
        return count == null ? 0 : count;
    }

    /**
     * 判断当前节点连接数是否到达上限
     *
     * @return 是否到达上限 true
     */
    public boolean overLimit() {
        String nodeName = serverConfig.getNodeName();
        ServerInfo serverInfo = serverConfig.getServers().get(nodeName);
        if (connectCount >= serverInfo.getMaxConnect()) {
            return true;
        }
        return false;
    }
}
