package com.cn.boot.sample.guava.io;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Chen Nan
 */
@NoArgsConstructor
@Data
public class ProductModuleConfigVO {
    @JSONField(name = "identifier")
    private String identifier;
    @JSONField(name = "name")
    private String name;
    @JSONField(name = "group")
    private String group;
}
