package com.cn.boot.sample.business.mock;

import com.alibaba.fastjson.annotation.JSONField;
import com.forte.util.mapper.MockValue;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Chen Nan
 */
@Data
@NoArgsConstructor
public class IotData1 {

    @JSONField(name = "Hum")
    @MockValue("@integer(2)")
    private Integer hum;

    @JSONField(name = "Cur")
    @MockValue("@doubles(10,20)")
    private Double cur;

    @JSONField(name = "CaptureTime")
//    @MockValue("@timestamp")
    private Long captureTime;
}
