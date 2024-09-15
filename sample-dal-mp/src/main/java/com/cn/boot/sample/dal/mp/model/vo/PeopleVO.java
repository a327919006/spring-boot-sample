package com.cn.boot.sample.dal.mp.model.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@ApiModel(value = "PeopleVO")
public class PeopleVO implements Serializable {

    @ApiModelProperty("ID")
    private Long id;

    @ApiModelProperty("姓名")
    private String name;

    @ApiModelProperty("账户余额")
    private BigDecimal account;

    @ApiModelProperty("创建时间")
    private LocalDateTime createTime;

    @ApiModelProperty("更新时间")
    private LocalDateTime updateTime;
}
