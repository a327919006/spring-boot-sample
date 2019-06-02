package com.cn.boot.sample.api.model.dto.client;

import cn.hutool.json.JSONUtil;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * @author Chen Nan
 */
@ApiModel
@Getter
@Setter
public class ClientAddReq implements Serializable {
    @ApiModelProperty(value = "id")
    private Long id;

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

    @Override
    public String toString() {
        return JSONUtil.toJsonStr(this);
    }
}
