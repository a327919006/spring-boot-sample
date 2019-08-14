package com.cn.boot.sample.wechat.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

/**
 * @author Chen Nan
 */
@ApiModel
@Data
public class CheckMsgDTO implements Serializable {
    @ApiModelProperty(value = "微信加密签名", required = true, notes = "signature结合了开发者填写的token参数和请求中的timestamp参数、nonce参数。")
    @NotBlank
    private String signature;

    @ApiModelProperty(value = "时间戳", required = true)
    @NotBlank
    private String timestamp;

    @ApiModelProperty(value = "随机数", required = true)
    @NotBlank
    private String nonce;

    @ApiModelProperty(value = "随机字符串", required = true)
    @NotBlank
    private String echostr;
}
