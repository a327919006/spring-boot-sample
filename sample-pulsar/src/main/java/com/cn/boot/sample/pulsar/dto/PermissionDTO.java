package com.cn.boot.sample.pulsar.dto;

import lombok.Data;
import org.apache.pulsar.common.policies.data.AuthAction;

import java.util.Set;

/**
 * @author Chen Nan
 */
@Data
public class PermissionDTO {
    private String topic;
    private String role;
    private Set<AuthAction> action;
}
