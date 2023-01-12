package com.cn.boot.sample.pulsar.controller;

import com.cn.boot.sample.api.model.dto.RspBase;
import com.cn.boot.sample.pulsar.dto.TenantInfoDTO;
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
@RequestMapping("/tenant")
@Api(tags = "1、租户", produces = MediaType.APPLICATION_JSON_VALUE)
public class TenantController {

    @Autowired
    private PulsarAdmin client;

    @ApiOperation("创建")
    @PostMapping("/")
    public RspBase<String> create(TenantInfoDTO dto) throws PulsarAdminException {
        TenantInfo tenantInfo = TenantInfo.builder()
                .adminRoles(dto.getRoles())
                .allowedClusters(dto.getClusters())
                .build();

        client.tenants().createTenant(dto.getTenantName(), tenantInfo);
        return RspBase.success();
    }

    @ApiOperation("列表")
    @GetMapping("/")
    public RspBase<List<String>> list() throws PulsarAdminException {
        return RspBase.data(client.tenants().getTenants());
    }

    @ApiOperation("删除")
    @DeleteMapping("/")
    public RspBase<String> delete(String tenant) throws PulsarAdminException {
        client.tenants().deleteTenant(tenant);
        return RspBase.success();
    }
}
