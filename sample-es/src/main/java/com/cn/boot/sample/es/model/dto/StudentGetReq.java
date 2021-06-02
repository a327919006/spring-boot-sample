package com.cn.boot.sample.es.model.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @author Chen Nan
 */
@ApiModel
@Data
public class StudentGetReq implements Serializable {

    @ApiModelProperty(value = "姓名")
    private List<String> nameList;

    @ApiModelProperty(value = "年龄")
    private Integer age;
}