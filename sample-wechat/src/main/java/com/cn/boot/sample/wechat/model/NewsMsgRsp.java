package com.cn.boot.sample.wechat.model;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * @author Chen Nan
 */
@EqualsAndHashCode(callSuper = true)
@Data
@ApiModel
@JacksonXmlRootElement(localName = "xml")
public class NewsMsgRsp extends BaseMsgRsp {

    @JacksonXmlProperty(localName = "ArticleCount")
    @ApiModelProperty(value = "图文消息个数")
    private String articleCount;

    @JacksonXmlElementWrapper(localName ="Articles")
    @JacksonXmlProperty(localName = "item")
    @ApiModelProperty(value = "图文消息信息")
    private List<ArticleMsgRsp> articles;


    public NewsMsgRsp(ReceiveMsgDTO req, List<ArticleMsgRsp> articles) {
        super(req);
        setMsgType("news");
        setArticleCount(articles.size() + "");
        setArticles(articles);
    }
}
