package com.cn.boot.sample.tdengine.model.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @author Chen Nan
 */
@Data
public class IotDataDTO {
    private String deviceId;
    private List<String> measurements;
    private List<String> values;
    @ApiModelProperty(value = "多久之前，单位毫秒", required = false, notes = "默认为获取1小时内数据")
    private Long before;
}
