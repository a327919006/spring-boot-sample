package com.cn.boot.sample.api.model.dto.client;

import cn.hutool.json.JSONUtil;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * @author Chen Nan
 */
@ApiModel
@Getter
@Setter
public class ClientSearchReq implements Serializable {
    @ApiModelProperty(value = "id", required = true)
    @NotNull
    private String id;

    private Integer page;
    private Integer rows;

    @Override
    public String toString() {
        return JSONUtil.toJsonStr(this);
    }
}
