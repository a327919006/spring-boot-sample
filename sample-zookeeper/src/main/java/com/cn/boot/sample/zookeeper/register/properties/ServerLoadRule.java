package com.cn.boot.sample.zookeeper.register.properties;

import lombok.Data;

/**
 * 节点负载规则
 *
 * @author Chen Nan
 */
@Data
public class ServerLoadRule {
    private LoadRuleInfo cpu;
    private LoadRuleInfo connect;
}
