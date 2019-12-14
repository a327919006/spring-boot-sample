package com.cn.boot.sample.swagger.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

/**
 * @author Chen Nan
 */
@Data
@ApiModel
public class TestReq implements Serializable {
    @NotBlank
    @ApiModelProperty(value = "姓名", required = true)
    private String name;
}
