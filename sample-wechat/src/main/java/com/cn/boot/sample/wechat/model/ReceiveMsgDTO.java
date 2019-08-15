package com.cn.boot.sample.wechat.model;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author Chen Nan
 */
@ApiModel
@JacksonXmlRootElement(localName = "xml")
@Data
public class ReceiveMsgDTO {

    @JacksonXmlProperty(localName = "ToUserName")
    @ApiModelProperty(value = "开发者微信号")
    private String toUserName;

    @JacksonXmlProperty(localName = "FromUserName")
    @ApiModelProperty(value = "发送方帐号（一个OpenID）")
    private String fromUserName;

    @JacksonXmlProperty(localName = "CreateTime")
    @ApiModelProperty(value = "消息创建时间 （整型）")
    private String createTime;

    @JacksonXmlProperty(localName = "MsgType")
    @ApiModelProperty(value = "消息类型，文本为text、图片为image、语音为voice、视频为video、小视频为shortvideo、位置为location、连接为link")
    private String msgType;

    @JacksonXmlProperty(localName = "Event")
    @ApiModelProperty(value = "事件类型，CLICK/VIEW/scancode_push等等")
    private String event;

    @JacksonXmlProperty(localName = "EventKey")
    @ApiModelProperty(value = "事件KEY值，与自定义菜单接口中KEY值对应")
    private String eventKey;

    @JacksonXmlProperty(localName = "Content")
    @ApiModelProperty(value = "文本消息内容")
    private String content;

    @JacksonXmlProperty(localName = "MsgId")
    @ApiModelProperty(value = "消息id，64位整型，用户在公众号发送的消息")
    private String msgId;

    @JacksonXmlProperty(localName = "MsgID")
    @ApiModelProperty(value = "消息id，模板消息")
    private String msgID;

    @JacksonXmlProperty(localName = "Status")
    @ApiModelProperty(value = "模板消息发送状态 success成功、failed:user block用户拒收、failed: system failed其他原因")
    private String status;

    @JacksonXmlProperty(localName = "PicUrl")
    @ApiModelProperty(value = "图片链接（由系统生成）")
    private String picUrl;

    @JacksonXmlProperty(localName = "MediaId")
    @ApiModelProperty(value = "图片消息媒体id，可以调用获取临时素材接口拉取数据。")
    private String mediaId;

    @JacksonXmlProperty(localName = "Format")
    @ApiModelProperty(value = "语音格式，如amr，speex等")
    private String format;

    @JacksonXmlProperty(localName = "Recognition")
    @ApiModelProperty(value = "语音识别结果，UTF8编码")
    private String recognition;

    @JacksonXmlProperty(localName = "ThumbMediaId")
    @ApiModelProperty(value = "视频消息缩略图的媒体id，可以调用多媒体文件下载接口拉取数据。")
    private String thumbMediaId;

    @JacksonXmlProperty(localName = "Location_X")
    @ApiModelProperty(value = "地理位置维度")
    private String locationX;

    @JacksonXmlProperty(localName = "Location_Y")
    @ApiModelProperty(value = "地理位置经度")
    private String locationY;

    @JacksonXmlProperty(localName = "Scale")
    @ApiModelProperty(value = "地图缩放大小")
    private String scale;

    @JacksonXmlProperty(localName = "Label")
    @ApiModelProperty(value = "地理位置信息")
    private String label;

    @JacksonXmlProperty(localName = "Title")
    @ApiModelProperty(value = "消息标题")
    private String title;

    @JacksonXmlProperty(localName = "Description")
    @ApiModelProperty(value = "消息描述")
    private String description;

    @JacksonXmlProperty(localName = "Url")
    @ApiModelProperty(value = "消息链接")
    private String url;

    public String getMsgId() {
        return msgId;
    }

    public String getMsgID() {
        return msgID;
    }

}
