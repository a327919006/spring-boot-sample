package com.cn.boot.sample.redis.dto;

import lombok.Data;

import java.util.List;

/**
 * @author Chen Nan
 */
@Data
public class ScrollResult {
    private List<?> list;
    private Long minTime;
    private Integer offset;
}
