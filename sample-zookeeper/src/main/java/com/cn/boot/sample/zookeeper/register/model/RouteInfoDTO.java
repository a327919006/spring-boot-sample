package com.cn.boot.sample.zookeeper.register.model;

import lombok.Data;

import java.util.List;

/**
 * @author Chen Nan
 */
@Data
public class RouteInfoDTO {
    private List<String> serverList;
    private Integer serverPort;
}
