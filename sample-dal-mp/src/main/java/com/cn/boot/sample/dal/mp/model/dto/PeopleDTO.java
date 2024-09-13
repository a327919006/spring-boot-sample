package com.cn.boot.sample.dal.mp.model.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@ApiModel(value = "People对象", description = "人员信息")
public class PeopleDTO implements Serializable {

    @ApiModelProperty("ID")
    private Long id;

    @ApiModelProperty("姓名")
    private String name;
}
