package com.cn.boot.sample.wechat.service;

import com.cn.boot.sample.wechat.model.BaseMsgRsp;
import com.cn.boot.sample.wechat.model.CheckMsgDTO;
import com.cn.boot.sample.wechat.model.CreateMenuDTO;
import com.cn.boot.sample.wechat.model.ReceiveMsgDTO;

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
}
