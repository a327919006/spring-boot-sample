package com.cn.boot.sample.wechat.util;

import cn.hutool.http.HttpRequest;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.cn.boot.sample.wechat.config.properties.WechatProperties;
import com.cn.boot.sample.wechat.model.AccessToken;
import com.cn.boot.sample.wechat.model.CreateMenuDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Chen Nan
 */
@Slf4j
@Component
public class WechatUtils {
    @Autowired
    private WechatProperties wechatProperties;

    private static String accessTokenUrl = "https://api.weixin.qq.com/cgi-bin/token";
    private static String createMenuUrl = "https://api.weixin.qq.com/cgi-bin/menu/create?access_token=ACCESS_TOKEN";
    private static AccessToken accessToken;

    private void getToken() {
        log.info("【微信】获取AccessToken开始");
        Map<String, Object> param = new HashMap<>();
        param.put("grant_type", "client_credential");
        param.put("appid", wechatProperties.getAppId());
        param.put("secret", wechatProperties.getAppSecret());

        String result = HttpRequest.get(accessTokenUrl)
                .form(param)
                .execute().body();

        log.info("【微信】获取AccessToken完成,result={}", result);
        JSONObject jsonObject = JSONUtil.parseObj(result);
        String token = jsonObject.getStr("access_token");
        Integer expiresIn = jsonObject.getInt("expires_in");

        if (accessToken == null) {
            accessToken = new AccessToken();
        }
        accessToken.setAccessToken(token);
        accessToken.setExpiresTime(System.currentTimeMillis() + expiresIn * 1000);
    }

    /**
     * 获取accessToken
     *
     * @return accessToken
     */
    public String getAccessToken() {
        if (accessToken == null || accessToken.isExpired()) {
            getToken();
        }
        return accessToken.getAccessToken();
    }

    /**
     * 创建菜单
     *
     * @param req 菜单信息
     */
    public void createMenu(CreateMenuDTO req) {
        log.info("【微信】创建菜单开始");

        String result = HttpRequest.post(createMenuUrl.replace("ACCESS_TOKEN", getAccessToken()))
                .body(JSONUtil.toJsonStr(req))
                .execute().body();

        log.info("【微信】创建菜单完成,result={}", result);
    }
}
