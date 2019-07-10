package com.cn.boot.sample.security.core.config.properties;

import lombok.Data;

/**
 * @author Chen Nan
 */
@Data
public class OAuth2Properties {
    private String storeType = "jwt";
    private String jwtSigningKey = "sample";

    private OAuth2ClientPropertis[] clients = {};
}
