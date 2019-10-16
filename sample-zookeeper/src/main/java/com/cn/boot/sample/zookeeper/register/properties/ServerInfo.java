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
    private List<TagInfo> areaTags;
    private int maxConnect = 50000;
    private int maxCPU = 400;
    private String serverUri;
}
