package com.cn.boot.sample.dal.mp.model.vo;

import com.cn.boot.sample.dal.mp.enums.PeopleStatus;
import com.cn.boot.sample.dal.mp.model.po.Address;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * @author Chen Nan
 */
@Data
@ApiModel(value = "PeopleVO")
public class PeopleVO implements Serializable {

    @ApiModelProperty("ID")
    private Long id;

    @ApiModelProperty("姓名")
    private String name;

    @ApiModelProperty("账户余额")
    private BigDecimal account;

    @ApiModelProperty("地址")
    private Address address;

    @ApiModelProperty("状态 0待审核 1审核中 2审核成功 3审核失败")
    private PeopleStatus status;

    @ApiModelProperty("创建时间")
    private LocalDateTime createTime;

    @ApiModelProperty("更新时间")
    private LocalDateTime updateTime;
}
