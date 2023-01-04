package com.cn.boot.sample.pulsar.dto;

import lombok.Data;

import java.util.Set;

/**
 * @author Chen Nan
 */
@Data
public class TenantInfoDTO {
    private String tenantName;
    private Set<String> clusters;
    private Set<String> roles;
}
