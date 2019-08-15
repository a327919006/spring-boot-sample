package com.cn.boot.sample.wechat.service;

import com.cn.boot.sample.wechat.model.*;

/**
 * @author Chen Nan
 */
public interface WechatService {
    /**
     * 验证消息的确来自微信服务器
     *
     * @param req 验证参数
     * @return 验证结果
     */
    boolean check(CheckMsgDTO req);

    /**
     * 处理接收到的消息
     *
     * @param req 消息信息
     * @return 响应消息
     */
    BaseMsgRsp handleMsg(ReceiveMsgDTO req);

    /**
     * 获取AccessToken
     *
     * @return AccessToken
     */
    String getAccessToken();

    /**
     * 创建菜单
     *
     * @param req 菜单信息
     */
    void createMenu(CreateMenuDTO req);

    /**
     * 设置行业
     *
     * @param req 行业信息
     */
    void updateIndustry(UpdateIndustryDTO req);

    /**
     * 发送模板消息
     *
     * @param req 模板消息信息
     * @return 消息ID
     */
    long sendTemplateMsg(SendTemplateMsgDTO req);

    /**
     * 上传临时素材
     *
     * @param fileBytes 素材内容
     * @param type      媒体文件类型，分别有图片（image）、语音（voice）、视频（video）和缩略图（thumb）
     * @param fileName  文件名
     * @return 素材ID
     */
    String uploadMedia(byte[] fileBytes, String type, String fileName);

    /**
     * 创建二维码Ticket
     *
     * @param req 二维码信息
     * @return 二维码ticket
     */
    String createQrCodeTicket(CreateQrCodeTicketDTO req);

    /**
     * 获取用户基本信息
     *
     * @param openId 用户openId
     * @return 用户信息
     */
    UserInfoRsp getUserInfo(String openId);

    /**
     * 获取用户AccessToken
     *
     * @param code code
     * @return 用户token
     */
    UserTokenRsp getUserTokenByCode(String code);

    /**
     * 获取用户信息，根据AccessToken
     *
     * @param accessToken 用户accessToken
     * @param openid      openid
     * @return 用户信息
     */
    UserInfoRsp getUserInfoByToken(String accessToken, String openid);
}
