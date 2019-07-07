package com.cn.boot.sample.security.core.config.properties;

import lombok.Data;

/**
 * @author Chen Nan
 */
@Data
public class OAuth2ClientPropertis {
    private String clientId;
    private String clientSecret;
    /**
     * 默认0，永不过期
     */
    private int accessTokenValiditySeconds;
    private int refreshTokenValiditySeconds;
}
