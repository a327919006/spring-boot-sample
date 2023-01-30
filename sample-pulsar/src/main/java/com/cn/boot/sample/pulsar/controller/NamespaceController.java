package com.cn.boot.sample.pulsar.controller;

import com.cn.boot.sample.api.model.dto.RspBase;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.pulsar.client.admin.PulsarAdmin;
import org.apache.pulsar.client.admin.PulsarAdminException;
import org.apache.pulsar.common.policies.data.TenantInfo;
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
@RequestMapping("/namespace")
@Api(tags = "5、命名空间", produces = MediaType.APPLICATION_JSON_VALUE)
public class NamespaceController {

    @Autowired
    private PulsarAdmin client;

    @ApiOperation("创建")
    @PostMapping("/")
    public RspBase<String> create(String tenant, String namespace) throws PulsarAdminException {
        client.namespaces().createNamespace(tenant + "/" + namespace);
        return RspBase.success();
    }

    @ApiOperation("列表")
    @GetMapping("/")
    public RspBase<List<String>> list(String tenant) throws PulsarAdminException {
        return RspBase.data(client.namespaces().getNamespaces(tenant));
    }

    @ApiOperation("删除")
    @DeleteMapping("/")
    public RspBase<String> delete(String tenant, String namespace, boolean force) throws PulsarAdminException {
        client.namespaces().deleteNamespace(tenant + "/" + namespace, force);
        return RspBase.success();
    }
}
