package com.cn.boot.sample.zookeeper.register.properties;

import lombok.Data;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Chen Nan
 */
@Data
public class ServerConfig {
    private Map<String, ServerInfo> servers;
    private Integer serverPort;
    private String nodeName;
    private String parentNode;
    private ServerLoadRule loadRule;

}
