package com.cn.boot.sample.guava.io;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Chen Nan
 */
@NoArgsConstructor
@Data
public class ModuleConfigVO {
    @JSONField(name = "device_did")
    private String deviceDid;
    @JSONField(name = "device_name")
    private String deviceName;
    @JSONField(name = "config_content")
    private String configContent;
    @JSONField(name = "module_config")
    private String moduleConfig;
    @JSONField(name = "module_identify")
    private String moduleIdentify;
    @JSONField(name = "module_num")
    private String moduleNum;
}
