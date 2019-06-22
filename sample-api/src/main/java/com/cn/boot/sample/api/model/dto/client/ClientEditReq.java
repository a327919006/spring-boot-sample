package com.cn.boot.sample.api.model.dto.client;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * @author Chen Nan
 */
@ApiModel
@Data
public class ClientEditReq implements Serializable {
    @ApiModelProperty(value = "商户名称", required = true)
    @NotBlank
    private String name;

    @ApiModelProperty(value = "算法类型(0:腾讯)", required = true, allowableValues = "0")
    @NotNull
    private Byte thirdType;

    @ApiModelProperty(value = "算法APPID", required = true)
    @NotBlank
    private String thirdAppId;

    @ApiModelProperty(value = "算法SecretId", required = true)
    @NotBlank
    private String thirdSecretId;

    @ApiModelProperty(value = "算法SecretKey", required = true)
    @NotBlank
    private String thirdSecretKey;

    @ApiModelProperty(value = "算法UserId", required = true)
    @NotBlank
    private String thirdUserId;

    @ApiModelProperty(value = "状态（0-无效，1-有效）", required = true, allowableValues = "0,1")
    @Min(0)
    @Max(1)
    private byte status;
}
