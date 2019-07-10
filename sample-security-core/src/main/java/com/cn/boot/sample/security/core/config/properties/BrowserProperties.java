package com.cn.boot.sample.security.core.config.properties;

import com.cn.boot.sample.security.core.enums.LoginTypeEnum;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * 浏览器配置
 *
 * @author Chen Nan
 */
@Data
@ConfigurationProperties(prefix = "boot.sample.security.browser")
public class BrowserProperties {
    private String loginPage = "/default-login.html";
    private String logoutPage = "";

    private LoginTypeEnum loginType = LoginTypeEnum.JSON;

    private int rememberMeSeconds = 3600;
}
