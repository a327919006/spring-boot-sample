package com.cn.boot.sample.pulsar.controller;

import com.cn.boot.sample.api.model.dto.RspBase;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.pulsar.client.admin.PulsarAdmin;
import org.apache.pulsar.client.admin.PulsarAdminException;
import org.apache.pulsar.common.policies.data.ClusterData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author Chen Nan
 */
@Slf4j
@RestController
@RequestMapping("/cluster")
@Api(tags = "2、集群", produces = MediaType.APPLICATION_JSON_VALUE)
public class ClusterController {

    @Autowired
    private PulsarAdmin client;

    @ApiOperation(value = "列表")
    @GetMapping("/list")
    public RspBase<List<String>> list() throws PulsarAdminException {
        return RspBase.data(client.clusters().getClusters());
    }

    @ApiOperation(value = "获取配置")
    @GetMapping("/config")
    public RspBase<ClusterData> get(String cluster) throws PulsarAdminException {
        return RspBase.data(client.clusters().getCluster(cluster));
    }
}
