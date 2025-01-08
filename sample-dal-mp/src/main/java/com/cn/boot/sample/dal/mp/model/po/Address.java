package com.cn.boot.sample.dal.mp.model.po;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * <p>
 * 人员地址信息
 * </p>
 *
 * @author Chen Nan
 * @since 2024-09-08
 */
@Data
@Accessors(chain = true)
@ApiModel(value = "Address对象", description = "地址信息")
public class Address implements Serializable {
    @ApiModelProperty("省")
    private String province;
    @ApiModelProperty("市")
    private String city;
    @ApiModelProperty("区")
    private String area;
}
