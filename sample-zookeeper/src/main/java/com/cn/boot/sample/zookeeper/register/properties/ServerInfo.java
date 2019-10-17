package com.cn.boot.sample.zookeeper.register.properties;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author Chen Nan
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ServerInfo {
    private List<TagInfo> appTags;
    private List<TagInfo> serverTags;
    private int maxConnect = 50000;
    private double maxCPU = 80;
    private String serverUri;
}
