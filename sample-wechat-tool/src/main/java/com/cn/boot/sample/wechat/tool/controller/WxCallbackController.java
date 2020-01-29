package com.cn.boot.sample.wechat.tool.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import me.chanjar.weixin.common.error.WxErrorException;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.bean.result.WxMpOAuth2AccessToken;
import me.chanjar.weixin.mp.bean.result.WxMpUser;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Edward
 */
@RestController
@RequestMapping("/wx/code")
@Api(tags = "微信网页授权", produces = MediaType.APPLICATION_JSON_VALUE)
public class WxCallbackController {

    @Autowired
    private WxMpService wxService;

    @ApiOperation(value = "授权回调")
    @GetMapping("/callback")
    public Object greetUser(@RequestParam String code, @RequestParam String state, ModelMap map) {
        try {
            WxMpOAuth2AccessToken accessToken = wxService.oauth2getAccessToken(code);
            if (StringUtils.endsWithIgnoreCase(state, "userinfo")) {
                WxMpUser user = wxService.oauth2getUserInfo(accessToken, null);
                map.put("user", user);
                return user;
            }
            return accessToken.getOpenId();

        } catch (WxErrorException e) {
            e.printStackTrace();
        }

        return "error";
    }
}
