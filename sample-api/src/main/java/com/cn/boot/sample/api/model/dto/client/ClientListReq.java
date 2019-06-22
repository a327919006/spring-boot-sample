package com.cn.boot.sample.api.model.dto.client;

import cn.hutool.json.JSONUtil;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @author Chen Nan
 */
@ApiModel
@Data
public class ClientListReq implements Serializable {
    @ApiModelProperty(value = "商户名称", required = true)
    private String name;

    private Integer pageNum;
    private Integer pageSize;

    /**
     * 是否需要计算总数
     */
    @ApiModelProperty(hidden = true)
    private Boolean count = false;
    /**
     * 排序
     */
    @ApiModelProperty(hidden = true)
    private String orderBy;

    @Override
    public String toString() {
        return JSONUtil.toJsonStr(this);
    }
}
