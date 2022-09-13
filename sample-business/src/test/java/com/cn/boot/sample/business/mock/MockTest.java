package com.cn.boot.sample.business.mock;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.IdUtil;
import com.alibaba.fastjson.JSONObject;
import com.forte.util.Mock;
import com.forte.util.mockbean.MockObject;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Chen Nan
 */
@Slf4j
public class MockTest {

    @Test
    public void mockTest() {
        IotDataBase iotDataBase = new IotDataBase();
        iotDataBase.setId(IdUtil.simpleUUID());
        iotDataBase.setTopic("/alink/123/AIR-1/module/property/post");
        iotDataBase.getParams().setTime(System.currentTimeMillis());

        Mock.set(IotData1.class);
        Mock.set(IotData2.class);
        MockObject<IotData1> mockValue1 = Mock.get(IotData1.class);
        IotData1 value1 = mockValue1.get().get();
        iotDataBase.getParams().getValue().put("STATUS", value1);
        log.info(JSONObject.toJSONString(iotDataBase));

        MockObject<IotData2> mockValue2 = Mock.get(IotData2.class);
        IotData2 value2 = mockValue2.get().get();
        iotDataBase.getParams().getValue().put("STATUS", value2);
        log.info(JSONObject.toJSONString(iotDataBase));
    }


    @Test
    public void mockMapTest() {
        IotDataBase iotDataBase = new IotDataBase();
        iotDataBase.setId(IdUtil.simpleUUID());
        iotDataBase.setTopic("/alink/123/AIR-1/module/property/post");
        iotDataBase.getParams().setTime(System.currentTimeMillis());

        // 准备模板载体
        Map<String, Object> template = new HashMap<>();
        //name是一个随机的中文名称。
        template.put("name", "@cname");
        //age 是一个18-80之间的随机数。
        template.put("age|10-80", 0);
        // 随机浮点数
        template.put("status0", "@doubles(10,80)");
        // 随机1-100
        template.put("status1", "@integer(1,100)");
        // 随机两位数
        template.put("status2", "@integer(2)");
        // 定值
        template.put("status3", "-1");
        //email是一个随机的163邮箱。
        template.put("email", "@email(163,com)");
        //password是一个6-16位数的随机字符。
        template.put("password", "@word(6,16)");

        // 设置
        Mock.set("user", template);
        // 获取一个MockObject
        MockObject<Map> mockUser = Mock.get("user");
        Map userdata = mockUser.getOne();

        iotDataBase.getParams().getValue().put("STATUS", userdata);
        log.info(JSONObject.toJSONString(iotDataBase));
    }
}
