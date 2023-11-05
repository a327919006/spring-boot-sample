package com.cn.boot.sample.guava.io;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * @author Chen Nan
 */
@AllArgsConstructor
@Data
public class LogTime {
    private Double cost;
    private String datetime;
    private int times;
}
