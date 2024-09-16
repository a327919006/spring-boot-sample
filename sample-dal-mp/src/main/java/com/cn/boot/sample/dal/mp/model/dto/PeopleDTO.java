package com.cn.boot.sample.dal.mp.model.dto;

import com.cn.boot.sample.dal.mp.enums.PeopleStatus;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @author Chen Nan
 */
@Data
@ApiModel(value = "PeopleDTO")
public class PeopleDTO implements Serializable {

    @ApiModelProperty("ID")
    private Long id;

    @ApiModelProperty("姓名")
    private String name;

    @ApiModelProperty("账户余额")
    private BigDecimal account;

    @ApiModelProperty("状态 0待审核 1审核中 2审核成功 3审核失败")
    private PeopleStatus status;

    @ApiModelProperty("当前页码")
    private Long current;
    @ApiModelProperty("分页数量")
    private Long size;
}
