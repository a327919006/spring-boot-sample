package com.cn.boot.sample.iotdb.model.dto;

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
    private Long before;
}
