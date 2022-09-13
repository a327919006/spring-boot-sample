package com.cn.boot.sample.business.mock;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.annotation.JSONField;
import com.forte.util.mapper.MockValue;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Chen Nan
 */
@NoArgsConstructor
@Data
public class IotDataBase {

    @JSONField(name = "method", ordinal = 0)
    private String method = "module.property.post";
    @JSONField(name = "topic", ordinal = 1)
    private String topic;
    @JSONField(name = "id", ordinal = 2)
    @MockValue("@UUID")
    private String id;
    @JSONField(name = "sys", ordinal = 3)
    private SysDTO sys = new SysDTO();
    @JSONField(name = "params", ordinal = 4)
    private ParamsDTO params = new ParamsDTO();
    @JSONField(name = "version", ordinal = 5)
    private String version = "v1.7";

    @NoArgsConstructor
    @Data
    public static class SysDTO {
        @JSONField(name = "ack")
        private Integer ack = 1;
    }

    @NoArgsConstructor
    @Data
    public static class ParamsDTO {
        @JSONField(name = "time", ordinal = 0)
        private Long time;
        @JSONField(name = "value", ordinal = 1)
        private JSONObject value = new JSONObject();
    }
}
