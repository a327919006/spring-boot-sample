package com.cn.boot.sample.wechat.model;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * @author Chen Nan
 */
@Data
@AllArgsConstructor
@ApiModel
public class ArticleMsgRsp {

    @JacksonXmlProperty(localName = "Title")
    @ApiModelProperty(value = "图文消息标题")
    private String title;

    @JacksonXmlProperty(localName = "Description")
    @ApiModelProperty(value = "图文消息描述")
    private String description;

    @JacksonXmlProperty(localName = "PicUrl")
    @ApiModelProperty(value = "图片链接，支持JPG、PNG格式，较好的效果为大图360*200，小图200*200")
    private String picUrl;

    @JacksonXmlProperty(localName = "Url")
    @ApiModelProperty(value = "点击图文消息跳转链接")
    private String url;

}
