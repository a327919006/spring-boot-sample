package com.cn.boot.sample.iotdb.controller;

import cn.hutool.core.util.RandomUtil;
import com.cn.boot.sample.api.model.Constants;
import com.google.common.collect.Lists;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.iotdb.session.pool.SessionDataSetWrapper;
import org.apache.iotdb.session.pool.SessionPool;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;

/**
 * 一、性能测试
 * 1、写入性能
 * 1.1、空库
 * 共100万次请求 200并发 单设备/单属性 TPS=26270/s  共100万个指标值 总耗时43秒    磁盘34M
 * 共100万次请求 200并发 单设备/10属性 TPS=25863/s  共1000万个指标值 总耗时43秒   磁盘132M
 * 共100万次请求 10并发  10设备/单属性 TPS=8654/s   共1000万个指标值 总耗时118秒  磁盘31M
 * 共100万次请求 10并发  10设备/10属性 TPS=3731/s   共1亿个指标值  总耗时272秒  磁盘313M
 * 说明：将单个设备多个属性合并成一次请求，能提高写入效率，CPU 180%
 * <p>
 * 1.2、已有2亿条数据
 * 共100万次请求 10并发  10设备/单属性 TPS=8529/s   共1000个指标值 总耗时119秒  磁盘28M
 * 共100万次请求 10并发  10设备/10属性 TPS=3565/s   共1亿个指标值  总耗时283秒  磁盘318M
 * 说明：数据量对写入性能影响不大
 * <p>
 * 2、读取性能
 * 1.1、last
 * 共100万次请求 10并发  单设备/单属性  TPS=6620/s    总耗时151秒
 * 共100万次请求 50并发  单设备/单属性  TPS=22986/s   总耗时44秒
 * 共100万次请求 100并发 单设备/单属性 TPS=31227/s   总耗时33秒
 * 共100万次请求 200并发 单设备/单属性 TPS=31061/s   总耗时34秒
 * 共100万次请求 300并发 单设备/单属性 TPS=29441/s   总耗时37秒
 * 共100万次请求 400并发 单设备/单属性 TPS=29618/s   总耗时37秒
 * 共100万次请求 500并发 单设备/单属性 TPS=29673/s   总耗时37秒
 * 说明：CPU 390%
 * <p>
 * 3、混合读写性能
 * 共100万次请求 读100并发 写10并发 10设备/10属性 读TPS=26296/s 写TPS=1517/s
 * <p>
 * 二、稳定性测试
 * 1、高并发写
 * 单设备 200并发
 * 多设备 10并发
 * 说明：太高的并发会导致IotDB进入只读模式
 * 2、长时间
 *
 * @author Chen Nan
 */
@Slf4j
@RestController
@RequestMapping("/benchmark/jmeter")
@Api(tags = "jmeter压力测试", produces = MediaType.APPLICATION_JSON_VALUE)
public class JmeterTestController {

    @Autowired
    private SessionPool sessionPool;

    @ApiOperation("写入")
    @PostMapping("")
    public String insert(int single) throws Exception {
        int num = RandomUtil.randomInt(100);
        String deviceId = "root.device";
        String did = deviceId + num;
        long time = System.currentTimeMillis();
        String data = String.valueOf(RandomUtil.randomInt(10000));
        if (single == 1) {
            sessionPool.insertRecord(did, time,
                    Lists.newArrayList("property1"),
                    Lists.newArrayList(data)
            );
        } else {
            sessionPool.insertRecord(did, time,
                    Lists.newArrayList("property0", "property1", "property2", "property3", "property4", "property5",
                            "property6", "property7", "property8", "property9"),
                    Lists.newArrayList(data + 0, data + 1, data + 2, data + 3, data + 4, data + 5, data + 6, data + 7, data + 8, data + 9)
            );
        }
        return Constants.MSG_SUCCESS;
    }

    @ApiOperation("批量写入")
    @PostMapping("batch")
    public String batchInsert(int single) throws Exception {
        int num = RandomUtil.randomInt(100);
        String deviceId = "root.device";
        long time = System.currentTimeMillis();
        String data = String.valueOf(RandomUtil.randomInt(100));
        if (single == 1) {
            sessionPool.insertRecords(Lists.newArrayList(
                    deviceId + num + 1,
                    deviceId + num + 2,
                    deviceId + num + 3,
                    deviceId + num + 4,
                    deviceId + num + 5,
                    deviceId + num + 6,
                    deviceId + num + 7,
                    deviceId + num + 8,
                    deviceId + num + 9,
                    deviceId + num + 10
                    ),
                    Lists.newArrayList(time, time, time, time, time, time, time, time, time, time),
                    Lists.newArrayList(
                            Collections.singletonList("property1"),
                            Collections.singletonList("property2"),
                            Collections.singletonList("property3"),
                            Collections.singletonList("property4"),
                            Collections.singletonList("property5"),
                            Collections.singletonList("property6"),
                            Collections.singletonList("property7"),
                            Collections.singletonList("property8"),
                            Collections.singletonList("property9"),
                            Collections.singletonList("property10")
                    ),
                    Lists.newArrayList(
                            Collections.singletonList(data + 0),
                            Collections.singletonList(data + 1),
                            Collections.singletonList(data + 2),
                            Collections.singletonList(data + 3),
                            Collections.singletonList(data + 4),
                            Collections.singletonList(data + 5),
                            Collections.singletonList(data + 6),
                            Collections.singletonList(data + 7),
                            Collections.singletonList(data + 8),
                            Collections.singletonList(data + 9)
                    )
            );
        } else {
            sessionPool.insertRecords(Lists.newArrayList(
                    deviceId + num + 1,
                    deviceId + num + 2,
                    deviceId + num + 3,
                    deviceId + num + 4,
                    deviceId + num + 5,
                    deviceId + num + 6,
                    deviceId + num + 7,
                    deviceId + num + 8,
                    deviceId + num + 9,
                    deviceId + num + 10
                    ),
                    Lists.newArrayList(time, time, time, time, time, time, time, time, time, time),
                    Lists.newArrayList(
                            Lists.newArrayList("property0", "property1", "property2", "property3", "property4", "property5", "property6", "property7", "property8", "property9"),
                            Lists.newArrayList("property0", "property1", "property2", "property3", "property4", "property5", "property6", "property7", "property8", "property9"),
                            Lists.newArrayList("property0", "property1", "property2", "property3", "property4", "property5", "property6", "property7", "property8", "property9"),
                            Lists.newArrayList("property0", "property1", "property2", "property3", "property4", "property5", "property6", "property7", "property8", "property9"),
                            Lists.newArrayList("property0", "property1", "property2", "property3", "property4", "property5", "property6", "property7", "property8", "property9"),
                            Lists.newArrayList("property0", "property1", "property2", "property3", "property4", "property5", "property6", "property7", "property8", "property9"),
                            Lists.newArrayList("property0", "property1", "property2", "property3", "property4", "property5", "property6", "property7", "property8", "property9"),
                            Lists.newArrayList("property0", "property1", "property2", "property3", "property4", "property5", "property6", "property7", "property8", "property9"),
                            Lists.newArrayList("property0", "property1", "property2", "property3", "property4", "property5", "property6", "property7", "property8", "property9"),
                            Lists.newArrayList("property0", "property1", "property2", "property3", "property4", "property5", "property6", "property7", "property8", "property9")
                    ),
                    Lists.newArrayList(
                            Lists.newArrayList(data + 0, data + 1, data + 2, data + 3, data + 4, data + 5, data + 6, data + 7, data + 8, data + 9),
                            Lists.newArrayList(data + 10, data + 11, data + 12, data + 13, data + 14, data + 15, data + 16, data + 17, data + 18, data + 19),
                            Lists.newArrayList(data + 20, data + 21, data + 22, data + 23, data + 24, data + 25, data + 26, data + 27, data + 28, data + 29),
                            Lists.newArrayList(data + 30, data + 31, data + 32, data + 33, data + 34, data + 35, data + 36, data + 37, data + 38, data + 39),
                            Lists.newArrayList(data + 40, data + 41, data + 42, data + 43, data + 44, data + 45, data + 46, data + 47, data + 48, data + 49),
                            Lists.newArrayList(data + 50, data + 51, data + 52, data + 53, data + 54, data + 55, data + 56, data + 57, data + 58, data + 59),
                            Lists.newArrayList(data + 60, data + 61, data + 62, data + 63, data + 64, data + 65, data + 66, data + 67, data + 68, data + 69),
                            Lists.newArrayList(data + 70, data + 71, data + 72, data + 73, data + 74, data + 75, data + 76, data + 77, data + 78, data + 79),
                            Lists.newArrayList(data + 80, data + 81, data + 82, data + 83, data + 84, data + 85, data + 86, data + 87, data + 88, data + 89),
                            Lists.newArrayList(data + 90, data + 91, data + 92, data + 93, data + 94, data + 95, data + 96, data + 97, data + 98, data + 99)
                    )
            );
        }

        return Constants.MSG_SUCCESS;
    }

    @ApiOperation("list")
    @GetMapping("")
    public String list() throws Exception {
        int num = RandomUtil.randomInt(100);
        String deviceId = "root.device";
        String path = deviceId + num;
        String property = "property" + num;
        String sql = "select last " + property + " from " + path;
        SessionDataSetWrapper result = sessionPool.executeQueryStatement(sql);
        sessionPool.closeResultSet(result);
        return Constants.MSG_SUCCESS;
    }
}
