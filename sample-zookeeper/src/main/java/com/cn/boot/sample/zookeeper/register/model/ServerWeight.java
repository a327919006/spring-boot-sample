package com.cn.boot.sample.zookeeper.register.model;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * @author Chen Nan
 */
@Data
@AllArgsConstructor
public class ServerWeight implements Comparable<ServerWeight> {
    private String serverUri;
    private Integer weight;

    @Override
    public int compareTo(ServerWeight serverWeight) {
        return serverWeight.weight.compareTo(this.weight);
    }
}
