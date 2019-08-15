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
public class CodeCallbackDTO implements Serializable {
    @ApiModelProperty(value = "code", notes = "作为换取access_token的票据,每次用户授权带上的code将不一样，code只能使用一次，5分钟未被使用自动过期。")
    private String code;

    @ApiModelProperty(value = "时间戳")
    private String state;
}
