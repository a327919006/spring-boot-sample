package com.cn.boot.sample.wechat.model;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author Chen Nan
 */
@EqualsAndHashCode(callSuper = true)
@Data
@ApiModel
@JacksonXmlRootElement(localName = "xml")
public class TextMsgRsp extends BaseMsgRsp {

    @JacksonXmlProperty(localName = "Content")
    @ApiModelProperty(value = "回复的消息内容（换行：在content中能够换行，微信客户端就支持换行显示）")
    private String content;

    public TextMsgRsp(ReceiveMsgDTO req, String content) {
        super(req);
        setMsgType("text");
        setContent(content);
    }
}
