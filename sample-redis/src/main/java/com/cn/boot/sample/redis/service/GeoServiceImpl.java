package com.cn.boot.sample.redis.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.geo.Circle;
import org.springframework.data.geo.GeoResult;
import org.springframework.data.geo.GeoResults;
import org.springframework.data.geo.Point;
import org.springframework.data.redis.connection.RedisGeoCommands;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Chen Nan
 */
@Slf4j
@Service
public class GeoServiceImpl {

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    /**
     * 写入店铺位置
     * GEOADD key [NX | XX] [CH] longitude latitude member [longitude latitude member ...]
     *
     * @param type      店铺类型：ktv、商店、药店、停车场、加油站、酒店等等
     * @param shopId    店铺ID
     * @param longitude 经度
     * @param latitude  维度
     * @return 写入数量
     */
    public Long add(String type, String shopId, double longitude, double latitude) {
        String key = "geo:shop:" + type;
        // 注：支持批量写入
        return stringRedisTemplate.opsForGeo().add(key, new Point(longitude, latitude), shopId);
    }

    /**
     * 搜索附近店铺
     *
     * @param type      店铺类型
     * @param longitude 经度
     * @param latitude  维度
     * @param distance  搜索半径（单位：米）
     * @return 店铺ID和距离信息（单位：米）
     */
    public List<GeoResult<RedisGeoCommands.GeoLocation<String>>> searchPage(String type, double longitude, double latitude,
                                                                            double distance, int page) {
        int pageCount = 3;
        int from = (page - 1) * pageCount;
        int end = page * pageCount;
        String key = "geo:shop:" + type;
        GeoResults<RedisGeoCommands.GeoLocation<String>> radius =
                stringRedisTemplate.opsForGeo().radius(key, new Circle(longitude, latitude, distance),
                        RedisGeoCommands.GeoRadiusCommandArgs.newGeoRadiusArgs()
                                .includeDistance()
                                .sortAscending()
                                .limit(end)// 只支持指定end，from自己根据业务截取
                );
        if (radius == null) {
            return Collections.emptyList();
        }
        // 平均距离为所有搜索结果的平均距离，包含下面skip的
        log.info("平均距离：" + radius.getAverageDistance().getValue() + "米");

        List<GeoResult<RedisGeoCommands.GeoLocation<String>>> content = radius.getContent();
        if (content.size() <= from) {
            return Collections.emptyList();
        }
        List<GeoResult<RedisGeoCommands.GeoLocation<String>>> result = content.stream().skip(from).collect(Collectors.toList());
        return result;
    }
}
