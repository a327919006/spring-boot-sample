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
public class AppTagInfo {
    private String name;
    private int weight;
    private List<AreaTagInfo> areaTags;
}
