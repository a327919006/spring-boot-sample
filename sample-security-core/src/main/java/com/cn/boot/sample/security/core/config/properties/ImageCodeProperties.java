package com.cn.boot.sample.security.core.config.properties;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 图片验证码配置
 *
 * @author Chen Nan
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class ImageCodeProperties extends SmsCodeProperties {
    /**
     * 宽度
     */
    private Integer width = 110;
    /**
     * 高度
     */
    private Integer height = 30;

    public ImageCodeProperties() {
        setLength(4);
    }
}
