package com.cn.boot.sample.zookeeper.register.properties;

import lombok.Data;

/**
 * 负载规则
 *
 * @author Chen Nan
 */
@Data
public class LoadRuleInfo {
    private double step = 10;
    private int weight = -1;
}
