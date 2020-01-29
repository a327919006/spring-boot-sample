package com.cn.boot.sample.zookeeper.controller;

import cn.hutool.core.util.StrUtil;
import com.cn.boot.sample.api.model.Constants;
import com.cn.boot.sample.zookeeper.register.RegisterUtil;
import com.cn.boot.sample.zookeeper.register.RouteUtil;
import com.cn.boot.sample.zookeeper.register.constants.HazelcastConstant;
import com.cn.boot.sample.zookeeper.register.model.RouteInfoDTO;
import com.cn.boot.sample.zookeeper.register.monitor.ConnectMonitor;
import com.cn.boot.sample.zookeeper.register.monitor.CpuMonitor;
import com.cn.boot.sample.zookeeper.register.properties.ServerConfig;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.IMap;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

/**
 * @author Chen Nan
 */
@Slf4j
@RestController
@RequestMapping("/register")
@Api(tags = "注册中心测试", produces = MediaType.APPLICATION_JSON_VALUE)
public class RegisterController {

    @Autowired
    private ServerConfig serverConfig;
    @Autowired
    private RegisterUtil registerUtil;
    @Autowired
    private RouteUtil routeUtil;
    @Autowired
    private CpuMonitor cpuMonitor;
    @Autowired
    private ConnectMonitor connectMonitor;
    @Autowired
    private HazelcastInstance hazelcastInstance;

    @ApiOperation("获取配置")
    @GetMapping("/config")
    public ServerConfig getServerConfig() {
        return serverConfig;
    }

    @ApiOperation("获取在线服务节点列表")
    @GetMapping("/nodes")
    public Set<String> getNodeSet() {
        return registerUtil.getOnlineNodeSet();
    }

    @ApiOperation("获取当前节点CPU使用率")
    @GetMapping("/cpu")
    public double getCpu(String nodeName) {
        if (StrUtil.isBlank(nodeName)) {
            nodeName = serverConfig.getNodeName();
        }
        return cpuMonitor.getCpuRate(nodeName);
    }

    @ApiOperation("获取当前节点连接数")
    @GetMapping("/connect/count")
    public double getConnectCount(String nodeName) {
        if (StrUtil.isBlank(nodeName)) {
            nodeName = serverConfig.getNodeName();
        }
        return connectMonitor.getConnectCount(nodeName);
    }

    @ApiOperation("客户端路由")
    @GetMapping("/route")
    public RouteInfoDTO route(@RequestParam String appId, @RequestParam String server) {
        return routeUtil.route(appId, server);
    }

    @ApiOperation("模拟客户端发起长连接")
    @PostMapping("/connect")
    public String connect(String clientId) {
        // 判断连接数和CPU是否超过上限，作为服务器负载保护
        if (!connectMonitor.overLimit() && !cpuMonitor.overLimit()) {
            IMap<String, String> serverClient = hazelcastInstance.getMap(HazelcastConstant.MAP_SERVER_CLIENT);
            serverClient.put(clientId, serverConfig.getNodeName());
            return Constants.MSG_SUCCESS;
        }
        return Constants.MSG_FAIL;
    }
}
