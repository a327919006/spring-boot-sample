package com.cn.boot.sample.wechat.model;

import lombok.Data;

/**
 * @author Chen Nan
 */
@Data
public class AccessToken {
    /**
     * token
     */
    private String accessToken;
    /**
     * 过期时间
     */
    private Long expiresTime;

    /**
     * 判断token是否过期
     *
     * @return true过期 false未过期
     */
    public Boolean isExpired() {
        return System.currentTimeMillis() > expiresTime;
    }
}
