package com.cn.boot.sample.wechat.model;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author Chen Nan
 */
@Data
public class BaseMsgRsp {

    @JacksonXmlProperty(localName = "ToUserName")
    @ApiModelProperty(value = "接收方帐号（收到的OpenID）")
    private String toUserName;

    @JacksonXmlProperty(localName = "FromUserName")
    @ApiModelProperty(value = "开发者微信号")
    private String fromUserName;

    @JacksonXmlProperty(localName = "CreateTime")
    @ApiModelProperty(value = "消息创建时间 （整型）")
    private String createTime;

    @JacksonXmlProperty(localName = "MsgType")
    @ApiModelProperty(value = "消息类型，文本为text")
    private String msgType;

    public BaseMsgRsp(ReceiveMsgDTO req) {
        setToUserName(req.getFromUserName());
        setFromUserName(req.getToUserName());
        setCreateTime(System.currentTimeMillis() / 1000 + "");
    }
}
