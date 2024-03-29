package com.cn.boot.sample.guava.io;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.NumberUtil;
import com.alibaba.fastjson.JSONArray;
import com.google.common.io.Files;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.util.ResourceUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.util.*;

import static org.junit.jupiter.api.TestInstance.Lifecycle.PER_CLASS;


/**
 * 文件操作
 *
 * @author Chen Nan
 */
@Slf4j
@TestInstance(PER_CLASS)
public class OnlineParseTest {

    private File sourceFile;

    @BeforeAll
    public void init() throws FileNotFoundException {
        String sourceFilePath = "online.json";
        sourceFile = ResourceUtils.getFile("classpath:" + sourceFilePath);
    }

    /**
     * 按行处理
     */
    @Test
    public void readLines() throws IOException {
        String logStr = Files.asCharSource(sourceFile, StandardCharsets.UTF_8).read();
        List<OnlineData> onlineDataList = JSONArray.parseArray(logStr, OnlineData.class);
        String lastStatus = null;
        String firstStatus = null;
        String did = "500112000004";
        String deviceName = "重庆";
        Date lastTime = null;
        Date firstTime = null;
        Date startTime = DateUtil.parse("2023-12-02 00:00:00");
        Date endTime = DateUtil.parse("2023-12-21 00:00:00");
        long totalOfflineCount = 0;
        long totalOnlineCount = 0;
        long totalOfflineSecond = 0;
        long totalOnlineSecond = 0;
        long minOnlineSecond = Integer.MAX_VALUE;
        long minOfflineSecond = Integer.MAX_VALUE;
        long maxOnlineSecond = 0;
        long maxOfflineSecond = 0;

        Map<String, Integer> hourCount = new TreeMap<>();
        String format = "yyyy-MM-dd HH:mm:ss.SSS";
        for (OnlineData onlineData : onlineDataList) {
//            System.out.println(onlineData.getTs() + " " + (StringUtils.equals("online", onlineData.getStatus()) ? "上线" : "离线"));
            String currStatus = onlineData.getStatus();
            Date currTime = DateUtil.parse(onlineData.getTs());
            if (firstTime == null) {
                firstStatus = currStatus;
                firstTime = currTime;
            }
            if (lastStatus == null) {
                lastStatus = currStatus;
                lastTime = currTime;
                continue;
            }
            long time = currTime.getTime() - lastTime.getTime();
            String formatTime = DateUtil.formatBetween(time);
            if (StringUtils.equals("offline", lastStatus) && StringUtils.equals("online", currStatus)) {
//                System.out.println("'" + DateUtil.format(lastTime, format) + "\t" + "'" + DateUtil.format(currTime, format) + "\t离线\t" + formatTime);
                totalOfflineCount++;
                totalOfflineSecond += time;
                if (time < minOfflineSecond) {
                    minOfflineSecond = time;
                }
                if (time > maxOfflineSecond) {
                    maxOfflineSecond = time;
                }
                String key = StringUtils.leftPad(String.valueOf(currTime.getHours()), 2, "0");
                Integer count = hourCount.get(key);
                if (count == null) {
                    count = 1;
                } else {
                    count++;
                }
                hourCount.put(key, count);
            }
            if (StringUtils.equals("online", lastStatus) && StringUtils.equals("offline", currStatus)) {
//                System.out.println("'" + DateUtil.format(lastTime, format) + "\t" + "'" + DateUtil.format(currTime, format) + "\t在线\t" + formatTime);

                totalOnlineCount++;
                totalOnlineSecond += time;
                if (time < minOnlineSecond) {
                    minOnlineSecond = time;
                }
                if (time > maxOnlineSecond) {
                    maxOnlineSecond = time;
                }
            }
            lastStatus = currStatus;
            lastTime = currTime;
        }
        long lastSecond = endTime.getTime() - lastTime.getTime();
        long firstSecond = firstTime.getTime() - startTime.getTime();
        if (StringUtils.equals("offline", firstStatus)) {
            totalOnlineSecond += firstSecond;
        } else {
            totalOfflineSecond += firstSecond;
        }

        if (StringUtils.equals("offline", lastStatus)) {
            totalOfflineSecond += lastSecond;
        } else {
            totalOnlineSecond += lastSecond;
        }

        BigDecimal onlineRate = NumberUtil.round(Double.parseDouble(totalOnlineSecond + "") / (totalOnlineSecond + totalOfflineSecond) * 100, 2);
        long totalDay = (endTime.getTime() - startTime.getTime()) / 86400 / 1000;
        BigDecimal dayOfflineCount = NumberUtil.round(Double.parseDouble(totalOfflineCount + "") / totalDay, 2);


        printResult(deviceName, did, totalOfflineCount, dayOfflineCount, onlineRate, totalOnlineSecond, totalOfflineSecond, totalOnlineCount,
                maxOnlineSecond, maxOfflineSecond, minOnlineSecond, minOfflineSecond, hourCount);

        printResult2(deviceName, did, totalOfflineCount, dayOfflineCount, onlineRate, totalOnlineSecond, totalOfflineSecond, totalOnlineCount,
                maxOnlineSecond, maxOfflineSecond, minOnlineSecond, minOfflineSecond, hourCount);
    }

    private void printResult(String did, String deviceName, long totalOfflineCount, BigDecimal dayOfflineCount,
                             BigDecimal onlineRate, long totalOnlineSecond, long totalOfflineSecond,
                             long totalOnlineCount, long maxOnlineSecond, long maxOfflineSecond,
                             long minOnlineSecond, long minOfflineSecond, Map<String, Integer> hourCount) {
        System.out.print("站点名称\t");
        System.out.print("DID\t");
        System.out.print("在线率\t");
        System.out.print("离线次数\t");
        System.out.print("统计周期\t");
        System.out.print("总在线时长\t");
        System.out.print("总离线时长\t");
        System.out.print("平均在线时长\t");
        System.out.print("平均离线时长\t");
        System.out.print("最长在线时长\t");
        System.out.print("最长离线时长\t");
        System.out.print("最短在线时长\t");
        System.out.println("最短离线时长\t");

        System.out.print(deviceName + "\t");
        System.out.print(did + "\t");
        System.out.print(onlineRate + "%" + "\t");
        System.out.print(totalOfflineCount + "次,日均" + dayOfflineCount + "次" + "\t");
        System.out.print(DateUtil.formatBetween((totalOnlineSecond + totalOfflineSecond)) + "\t");
        System.out.print(StringUtils.substringBefore(DateUtil.formatBetween(totalOnlineSecond), "秒") + "秒" + "\t");
        System.out.print(StringUtils.substringBefore(DateUtil.formatBetween(totalOfflineSecond), "秒") + "秒" + "\t");
        System.out.print(StringUtils.substringBefore(DateUtil.formatBetween(totalOnlineSecond / totalOnlineCount), "秒") + "秒" + "\t");
        System.out.print(StringUtils.substringBefore(DateUtil.formatBetween(totalOfflineSecond / totalOfflineCount), "秒") + "秒" + "\t");
        System.out.print(StringUtils.substringBefore(DateUtil.formatBetween(maxOnlineSecond), "秒") + "秒" + "\t");
        System.out.print(StringUtils.substringBefore(DateUtil.formatBetween(maxOfflineSecond), "秒") + "秒" + "\t");
        System.out.print(DateUtil.formatBetween(minOnlineSecond) + "\t");
        System.out.println(DateUtil.formatBetween(minOfflineSecond) + "\t");
    }

    private void printResult2(String did, String deviceName, long totalOfflineCount, BigDecimal dayOfflineCount, BigDecimal onlineRate,
                              long totalOnlineSecond, long totalOfflineSecond,
                              long totalOnlineCount, long maxOnlineSecond, long maxOfflineSecond,
                              long minOnlineSecond, long minOfflineSecond, Map<String, Integer> hourCount) {
        System.out.println("在线率\t" + onlineRate + "%");
        System.out.println("离线次数\t" + totalOfflineCount + "次,日均" + dayOfflineCount + "次");
        System.out.println("统计周期\t" + DateUtil.formatBetween((totalOnlineSecond + totalOfflineSecond)));
        System.out.println("总在线时长\t" + StringUtils.substringBefore(DateUtil.formatBetween(totalOnlineSecond), "秒") + "秒");
        System.out.println("总离线时长\t" + StringUtils.substringBefore(DateUtil.formatBetween(totalOfflineSecond), "秒") + "秒");
        System.out.println("平均在线时长\t" + StringUtils.substringBefore(DateUtil.formatBetween(totalOnlineSecond / totalOnlineCount), "秒") + "秒");
        System.out.println("平均离线时长\t" + StringUtils.substringBefore(DateUtil.formatBetween(totalOfflineSecond / totalOfflineCount), "秒") + "秒");
        System.out.println("最长在线时长\t" + StringUtils.substringBefore(DateUtil.formatBetween(maxOnlineSecond), "秒") + "秒");
        System.out.println("最长离线时长\t" + StringUtils.substringBefore(DateUtil.formatBetween(maxOfflineSecond), "秒") + "秒");
        System.out.println("最短在线时长\t" + DateUtil.formatBetween(minOnlineSecond));
        System.out.println("最短离线时长\t" + DateUtil.formatBetween(minOfflineSecond));
        System.out.println(hourCount);
        for (String hour : hourCount.keySet()) {
//            System.out.println(hour + "点\t" + hourCount.get(hour));
        }
    }
}
