package com.cn.boot.sample.zookeeper.register.model;

import lombok.Data;

/**
 * @author Chen Nan
 */
@Data
public class RouteInfoDTO {
    private String serverList;
    private Integer serverPort;
}
