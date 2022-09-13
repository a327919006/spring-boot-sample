package com.cn.boot.sample.business.mock;

import com.alibaba.fastjson.annotation.JSONField;
import com.forte.util.mapper.MockValue;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Chen Nan
 */
@NoArgsConstructor
@Data
public class IotData2 {

    @JSONField(name = "DevHum")
    @MockValue("@doubles(20,30)")
    private Double devHum;

    @JSONField(name = "RunAllSta")
    @MockValue("@integer(2)")
    private Integer runAllSta;

    @JSONField(name = "ErrorCode")
    @MockValue("0")
    private String errorCode;
}
