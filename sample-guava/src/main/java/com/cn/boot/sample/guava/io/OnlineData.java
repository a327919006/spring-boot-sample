package com.cn.boot.sample.guava.io;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Chen Nan
 */
@NoArgsConstructor
@Data
public class OnlineData {
    private String ts;
    private String status;
    private String did;
}
