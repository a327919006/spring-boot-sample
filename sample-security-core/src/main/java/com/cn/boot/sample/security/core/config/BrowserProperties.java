package com.cn.boot.sample.security.core.config;

import com.cn.boot.sample.security.core.enums.LoginTypeEnum;
import lombok.Data;

/**
 * @author Chen Nan
 */
@Data
public class BrowserProperties {
    private String loginPage = "/default-login.html";

    private LoginTypeEnum loginType = LoginTypeEnum.JSON;
}
