package com.cn.boot.sample.es.model.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @author Chen Nan
 */
@ApiModel
@Data
public class StudentAddReq implements Serializable {
    @ApiModelProperty(value = "ID", required = true)
    @NotBlank
    private String id;

    @ApiModelProperty(value = "姓名", required = true)
    @NotBlank
    private String name;

    @ApiModelProperty(value = "年龄", required = true)
    @NotNull
    private Integer age;

    @ApiModelProperty(value = "创建时间")
    private LocalDateTime createTime;
}