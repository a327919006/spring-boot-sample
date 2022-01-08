package com.cn.boot.sample.iotdb.model.po;

import lombok.Data;

/**
 * @author Chen Nan
 */
@Data
public class Pile {
    private Long timestamp;
    private Float status;
    private Float power;
}
