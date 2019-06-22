package com.cn.boot.sample.api.model.dto.client;

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
public class ClientAddReq implements Serializable {
    @ApiModelProperty(value = "商户ID")
    private String id;

    @ApiModelProperty(value = "平台ID")
    @NotBlank
    private String platId;

    @ApiModelProperty(value = "商户名称", required = true)
    @NotBlank
    private String name;

    @ApiModelProperty(value = "第三方存储配置ID", required = true)
    @NotBlank
    private String ossConfigId;

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
}
