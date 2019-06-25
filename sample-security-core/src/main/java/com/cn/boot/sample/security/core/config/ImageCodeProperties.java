package com.cn.boot.sample.security.core.config;

import lombok.Data;

/**
 * 图片验证码配置
 *
 * @author Chen Nan
 */
@Data
public class ImageCodeProperties {
    /**
     * 宽度
     */
    private Integer width = 110;
    /**
     * 高度
     */
    private Integer height = 30;
    /**
     * 字符数
     */
    private Integer length = 4;
    /**
     * 过期时长 3分钟
     */
    private Long expire = 1000 * 60 * 3L;
    /**
     * 需要校验验证码的接口url
     */
    private String url;

}
