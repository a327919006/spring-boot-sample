package com.cn.boot.sample.api.model.dto.teacher;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * @author Chen Nan
 */
@ApiModel
@Data
public class TeacherAddReq implements Serializable {
    @ApiModelProperty(value = "姓名", required = true)
    @NotBlank
    private String name;

    @ApiModelProperty(value = "年龄", required = true)
    @NotNull
    private Integer age;

}
