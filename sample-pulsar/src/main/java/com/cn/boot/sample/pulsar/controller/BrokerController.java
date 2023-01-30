package com.cn.boot.sample.pulsar.controller;

import com.cn.boot.sample.api.model.dto.RspBase;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.apache.pulsar.client.admin.PulsarAdmin;
import org.apache.pulsar.client.admin.PulsarAdminException;
import org.apache.pulsar.common.conf.InternalConfigurationData;
import org.apache.pulsar.common.policies.data.BrokerInfo;
import org.apache.pulsar.common.policies.data.NamespaceOwnershipStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

/**
 * @author Chen Nan
 */
@Slf4j
@RestController
@RequestMapping("/broker")
@Api(tags = "3、broker", produces = MediaType.APPLICATION_JSON_VALUE)
public class BrokerController {

    @Autowired
    private PulsarAdmin client;

    @ApiOperation("列表")
    @GetMapping("/")
    public RspBase<List<String>> getActiveBrokers() throws PulsarAdminException {
        return RspBase.data(client.brokers().getActiveBrokers());
    }

    @ApiOperation("获取leaderBroker")
    @GetMapping("/leader")
    public RspBase<BrokerInfo> getLeaderBroker() throws PulsarAdminException {
        return RspBase.data(client.brokers().getLeaderBroker());
    }


    @ApiOperation("获取Broker所拥有的命名空间列表")
    @GetMapping("/namespaces")
    public RspBase<Map<String, NamespaceOwnershipStatus>> getOwnedNamespaces(String cluster, @ApiParam(example = "localhost:8080") String brokerUrl) throws PulsarAdminException {
        return RspBase.data(client.brokers().getOwnedNamespaces(cluster, brokerUrl));
    }

    @ApiOperation("获取当前所有配置")
    @GetMapping("/config/runtime")
    public RspBase<Map<String, String>> getRuntimeConfigurations() throws PulsarAdminException {
        return RspBase.data(client.brokers().getRuntimeConfigurations());
    }

    @ApiOperation("获取内部配置")
    @GetMapping("/config/internal")
    public RspBase<InternalConfigurationData> getInternalConfigurationData() throws PulsarAdminException {
        return RspBase.data(client.brokers().getInternalConfigurationData());
    }

    @ApiOperation("获取所有允许动态修改的配置")
    @GetMapping("/config/able")
    public RspBase<List<String>> getDynamicConfigurationNames() throws PulsarAdminException {
        return RspBase.data(client.brokers().getDynamicConfigurationNames());
    }

    @ApiOperation("动态更新broker配置")
    @PutMapping("/config")
    public RspBase<String> updateConfig(String configName, String configValue) throws PulsarAdminException {
        client.brokers().updateDynamicConfiguration(configName, configValue);
        return RspBase.success();
    }

    @ApiOperation("获取所有已经动态修改过的配置")
    @GetMapping("/config/changed")
    public RspBase<Map<String, String>> getAllDynamicConfigurations() throws PulsarAdminException {
        return RspBase.data(client.brokers().getAllDynamicConfigurations());
    }
}
