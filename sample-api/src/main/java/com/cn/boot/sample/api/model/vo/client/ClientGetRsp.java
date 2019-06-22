package com.cn.boot.sample.api.model.vo.client;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @author Chen Nan
 */
@Data
@ApiModel
public class ClientGetRsp implements Serializable {
    @ApiModelProperty("商户ID")
    private String id;

    @ApiModelProperty("平台ID")
    private String platId;

    @ApiModelProperty("商户名称")
    private String name;

    @ApiModelProperty("商户状态（0-无效，1-有效）")
    private Integer status;

    @ApiModelProperty("第三方算法类型 0腾讯 1瑞为 2华为")
    private Integer thirdType;

    @ApiModelProperty("第三方应用APPID")
    private String thirdAppId;

    @ApiModelProperty("第三方应用SecretId")
    private String thirdSecretId;

    @ApiModelProperty("第三方应用密钥")
    private String thirdSecretKey;

    @ApiModelProperty("第三方应用userId")
    private String thirdUserId;

    @ApiModelProperty("创建时间")
    private LocalDateTime createTime;

    @ApiModelProperty("更新时间")
    private LocalDateTime updateTime;
}
