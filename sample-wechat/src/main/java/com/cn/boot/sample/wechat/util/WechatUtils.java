package com.cn.boot.sample.wechat.util;

import cn.hutool.http.HttpRequest;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.cn.boot.sample.wechat.config.properties.WechatProperties;
import com.cn.boot.sample.wechat.model.*;
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

    private static final String ACCESS_TOKEN_URL = "https://api.weixin.qq.com/cgi-bin/token";
    private static final String CREATE_MENU_URL = "https://api.weixin.qq.com/cgi-bin/menu/create?access_token=ACCESS_TOKEN";
    private static final String UPDATE_INDUSTRY_URL = "https://api.weixin.qq.com/cgi-bin/template/api_set_industry?access_token=ACCESS_TOKEN";
    private static final String SEND_TEMPLATE_MSG_URL = "https://api.weixin.qq.com/cgi-bin/message/template/send?access_token=ACCESS_TOKEN";
    private static final String UPLOAD_MEDIA_URL = "https://api.weixin.qq.com/cgi-bin/media/upload?access_token=ACCESS_TOKEN&type=TYPE";
    private static AccessToken accessToken;

    private void getToken() {
        log.info("【微信】获取AccessToken开始");
        Map<String, Object> param = new HashMap<>();
        param.put("grant_type", "client_credential");
        param.put("appid", wechatProperties.getAppId());
        param.put("secret", wechatProperties.getAppSecret());

        String result = HttpRequest.get(ACCESS_TOKEN_URL)
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

        String result = HttpRequest.post(setUrlToken(CREATE_MENU_URL))
                .body(JSONUtil.toJsonStr(req))
                .execute().body();

        log.info("【微信】创建菜单完成,result={}", result);
    }

    /**
     * 创建菜单
     *
     * @param req 菜单信息
     */
    public void updateIndustry(UpdateIndustryDTO req) {
        log.info("【微信】设置行业开始");

        String result = HttpRequest.post(setUrlToken(UPDATE_INDUSTRY_URL))
                .body(JSONUtil.toJsonStr(req))
                .execute().body();

        log.info("【微信】设置行业完成,result={}", result);
    }

    /**
     * 发送模板消息
     *
     * @param req 消息内容
     * @return 消息ID
     */
    public long sendTemplateMsg(SendTemplateMsgDTO req) {
        log.info("【微信】发送模板消息开始,req={}", req);

        String result = HttpRequest.post(setUrlToken(SEND_TEMPLATE_MSG_URL))
                .body(JSONUtil.toJsonStr(req))
                .execute().body();

        log.info("【微信】发送模板消息完成,result={}", result);

        SendTemplateMsgRsp rsp = JSONUtil.toBean(result, SendTemplateMsgRsp.class);
        if (rsp.getErrcode() == 0) {
            return rsp.getMsgid();
        } else {
            throw new RuntimeException("发送失败");
        }
    }

    /**
     * 上传临时素材
     *
     * @param fileBytes 素材内容
     * @param type      媒体文件类型，分别有图片（image）、语音（voice）、视频（video）和缩略图（thumb）
     * @param fileName  文件名
     * @return 素材ID
     */
    public String uploadMedia(byte[] fileBytes, String type, String fileName) {
        log.info("【微信】上传临时素材开始");

        String result = HttpRequest.post(setUrlToken(UPLOAD_MEDIA_URL).replace("TYPE", type))
                .form("media", fileBytes, fileName)
                .execute().body();

        log.info("【微信】上传临时素材完成,result={}", result);

        UploadMediaRsp rsp = JSONUtil.toBean(result, UploadMediaRsp.class);
        if (rsp.getErrcode() == 0) {
            return rsp.getMedia_id();
        } else {
            throw new RuntimeException("上传失败");
        }
    }

    /**
     * 设置请求地址中的token参数
     *
     * @param url 请求地址
     * @return 地址+token
     */
    private String setUrlToken(String url) {
        return url.replace("ACCESS_TOKEN", getAccessToken());
    }
}
