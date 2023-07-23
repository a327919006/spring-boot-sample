package com.cn.boot.sample.api.model.dto.dingtalk;

import lombok.Data;

import java.util.List;

/**
 * @author Chen Nan
 */
@Data
public class RobotMsg {
    private Boolean isAtAll = false;
    private List<String> atMobiles;
}
