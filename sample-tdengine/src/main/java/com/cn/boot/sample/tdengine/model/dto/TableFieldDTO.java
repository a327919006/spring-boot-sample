package com.cn.boot.sample.tdengine.model.dto;

import lombok.Data;

/**
 * @author Chen Nan
 */
@Data
public class TableFieldDTO {
    private String Field;
    private String Type;
    private Integer Length;
    private String Note;
}
