package com.cn.boot.sample.business.station;

import cn.hutool.core.collection.ListUtil;
import cn.hutool.crypto.SecureUtil;
import cn.hutool.http.ContentType;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpHeaders;

import java.math.BigDecimal;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

/**
 * @author Chen Nan
 */
@Slf4j
public class FetchTest {

    private static final String SIGN_KEY = "#Ek4Gr8G1";
    private static String lat = "26.058528";
    private static String lng = "119.320528";

    /**
     * 获取城市经纬度坐标
     */
    @Test
    public void getCityLocation() {
        String url = "https://api.map.baidu.com/geocoder";
        Map<String, Object> param = new TreeMap<>();
        param.put("address", "福州市");
        param.put("output", "json");

        HttpResponse response = HttpRequest.get(url)
                .form(param)
                .execute();
        if (!response.isOk()) {
            log.error("request fail, status:{}", response.getStatus());
        }
        String body = response.body();

        CityLocationRsp rsp = JSON.parseObject(body, CityLocationRsp.class);
        if (!StringUtils.equalsIgnoreCase("OK", rsp.getStatus())) {
            log.error("request fail, body:{}", body);
        }
        CityLocationRsp.ResultDTO.LocationDTO location = rsp.getResult().getLocation();
        log.info("经度:{} 纬度:{}", location.getLng(), location.getLat());
    }

    /**
     * 根据经纬度获取城市信息
     * 国家、省、市、区、街道、路牌、详细地址等信息
     */
    @Test
    public void getCityInfo() {
        String url = "https://gateway.starcharge.com/app/api/xcx/getLocationByLatAndLng";
        Map<String, Object> param = new TreeMap<>();
        param.put("lat", lat);
        param.put("lng", lng);

        String timestamp = String.valueOf(System.currentTimeMillis());
        String sign = createSign(param, timestamp);

        Map<String, String> headerMap = new HashMap<>();
        headerMap.put(HttpHeaders.CONTENT_TYPE, ContentType.JSON.getValue());
        headerMap.put("X-Ca-Signature", sign);
        headerMap.put("X-Ca-Timestamp", timestamp);
        headerMap.put("appVersion", "6.6.0.2");

        HttpResponse response = HttpRequest.get(url)
                .form(param)
                .headerMap(headerMap, true)
                .execute();
        if (!response.isOk()) {
            log.error("request fail, status:{}", response.getStatus());
        }
        String body = response.body();

        StationCityRsp rsp = JSON.parseObject(body, StationCityRsp.class);
        if (!"200".equals(rsp.getCode())) {
            log.error("request fail, body:{}", body);
        }
        log.info(rsp.getData());
    }

    @Test
    public void testStationList() {
        stationList("350100", lat, lng);
    }

    private void stationList(String city, String lat, String lng) {
        String url = "https://gateway.starcharge.com/apph5/api/xcx/stubGroup/list";
        Map<String, Object> param = new TreeMap<>();
        param.put("city", city);
        param.put("lat", lat);
        param.put("lng", lng);
        param.put("userLat", lat);
        param.put("userLng", lng);
        param.put("radius", "10000");
        param.put("orderType", "1");
        param.put("page", "1");
        param.put("pagecount", "100");

        String timestamp = String.valueOf(System.currentTimeMillis());
        String sign = createSign(param, timestamp);

        Map<String, String> headerMap = new HashMap<>();
        headerMap.put(HttpHeaders.CONTENT_TYPE, ContentType.JSON.getValue());
        headerMap.put("X-Ca-Signature", sign);
        headerMap.put("X-Ca-Timestamp", timestamp);
        headerMap.put("appVersion", "6.6.0.2");

        HttpResponse response = HttpRequest.get(url)
                .form(param)
                .headerMap(headerMap, true)
                .execute();
        if (!response.isOk()) {
            log.error("request fail, status:{}", response.getStatus());
        }
        String body = response.body();
        log.info("body:{}", body);

        StationListRsp rsp = JSON.parseObject(body, StationListRsp.class);
        if (!"200".equals(rsp.getCode())) {
            log.error("request fail, body:{}", body);
        }
        List<StationListRsp.StationListDTO> list = rsp.getData();
        list.forEach(stationListDTO -> {
            // 站点ID
            log.info(stationListDTO.getId());
            log.info(JSON.toJSONString(stationListDTO));
        });
    }

    @Test
    public void testStationDetail() {
        stationDetail("MA01H3BQ1.MA002TMQX_350902MA002TMQX0002", lat, lng);
    }

    private void stationDetail(String id, String lat, String lng) {
        String url = "https://gateway.starcharge.com/apph5/api/xcx/stubGroup/stubGroupDetailNew/base/noUser";
        Map<String, Object> param = new TreeMap<>();
        param.put("gisType", "1");
        param.put("id", id);
        param.put("lat", lat);
        param.put("lng", lng);

        String timestamp = String.valueOf(System.currentTimeMillis());
        String sign = createSign(param, timestamp);

        Map<String, String> headerMap = new HashMap<>();
        headerMap.put(HttpHeaders.CONTENT_TYPE, ContentType.JSON.getValue());
        headerMap.put("X-Ca-Signature", sign);
        headerMap.put("X-Ca-Timestamp", timestamp);
        headerMap.put("appVersion", "6.6.0.2");

        HttpResponse response = HttpRequest.get(url)
                .form(param)
                .headerMap(headerMap, true)
                .execute();
        if (!response.isOk()) {
            log.error("request fail");
        }
        String body = response.body();
        log.info("body = {}", body);
        StationDetailRsp rsp = JSON.parseObject(body, StationDetailRsp.class);
        if (!"200".equals(rsp.getCode())) {
            log.error("request fail, body:{}", body);
        }
        // 费用信息
        if (rsp.getData().getTotalFeeInfoDiscounted() != null) {
            log.info(rsp.getData().getTotalFeeInfoDiscounted());
        }
        log.info(rsp.getData().getTotalFeeInfoEx());
    }

    @Test
    public void parseFeeInfo() {
        String feeInfo = "[[700,0.3004,0.3],[830,0.5732,0.3],[1130,0.8459,0.3],[1430,0.5732,0.3],[1730,0.8459,0.3],[1900,0.5732,0.3],[2100,0.8459,0.3],[2300,0.5732,0.3],[2359,0.3004,0.3]]";
        List<JSONArray> feeArray = JSON.parseArray(feeInfo, JSONArray.class);

        feeArray = feeArray.stream().filter(infoArray -> {
            if (infoArray.size() < 3) {
                return false;
            }
            Integer time = infoArray.getInteger(0);
            return time >= 10;
        }).collect(Collectors.toList());

        for (int i = 0; i < feeArray.size(); i++) {
            JSONArray infoArray = feeArray.get(i);
            String time = StringUtils.leftPad(infoArray.getString(0), 4, "0");
            BigDecimal electricPrice = infoArray.getBigDecimal(1);
            BigDecimal servicePrice = infoArray.getBigDecimal(2);

            LocalTime endTime = LocalTime.parse(time, DateTimeFormatter.ofPattern("HHmm"));
            System.out.println(endTime);
        }
        System.out.println(feeArray);
        System.out.println(LocalTime.parse("00:00"));
    }


    private String createSign(Map<String, Object> param, String timestamp) {
        String paramStr = paramToStr(param);
        paramStr += "#" + timestamp + SIGN_KEY;
        return SecureUtil.md5(paramStr).toUpperCase();
    }

    private String paramToStr(Map<String, Object> param) {
        AtomicReference<String> str = new AtomicReference<>("");
        param.forEach((k, v) -> str.set(str + k + "=" + v + "&"));
        return str.get().substring(0, str.get().length() - 1);
    }

    @Test
    public void test() {
        List<Integer> dataList = new ArrayList<>();
        for (int i = 0; i < 50; i++) {
            dataList.add(i);
        }

        int listStep = (int) Math.ceil(Double.parseDouble(String.valueOf(dataList.size())) / 49);
        List<List<Integer>> testSplit = ListUtil.split(dataList, listStep);
        List<Integer> testResult = testSplit.stream().map(list -> list.get(list.size() - 1)).collect(Collectors.toList());
        if (dataList.size() > 49) {
            testResult.add(0, testSplit.get(0).get(0));
        }
        System.out.println(testResult);
    }
}
