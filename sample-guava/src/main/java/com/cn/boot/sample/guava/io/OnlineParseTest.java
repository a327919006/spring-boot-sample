package com.cn.boot.sample.guava.io;

import cn.hutool.core.date.BetweenFormatter;
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
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

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
        String did = "500112000004";
        String deviceName = "重庆";
        Date startTime = DateUtil.parse("2023-12-02 00:00:00");
        Date endTime = DateUtil.parse("2023-12-09 00:00:00");
        OnlineData firstData = onlineDataList.get(0);

        Date firstTime = DateUtil.parse(firstData.getTs());
        String firstStatus = firstData.getStatus();
        Date lastTime = null;
        String lastStatus = null;
        long totalOfflineCount = 0;
        long totalOnlineCount = 0;
        long totalOfflineSecond = 0;
        long totalOnlineSecond = 0;
        long minOnlineSecond = endTime.getTime() - startTime.getTime();
        long minOfflineSecond = endTime.getTime() - startTime.getTime();
        long maxOnlineSecond = 0;
        long maxOfflineSecond = 0;

        if (firstTime.compareTo(startTime) > 0) {
            if (StringUtils.equals("online", firstStatus)) {
                totalOfflineCount++;
            } else {
                totalOnlineCount++;
            }
        }

        Map<String, Integer> hourCount = new TreeMap<>();
        for (OnlineData onlineData : onlineDataList) {
            String currStatus = onlineData.getStatus();
            Date currTime = DateUtil.parse(onlineData.getTs());
            if (StringUtils.equals("online", currStatus)) {
                totalOnlineCount++;
            } else {
                totalOfflineCount++;
            }
            if (lastStatus == null) {
                lastStatus = currStatus;
                lastTime = currTime;
                continue;
            }
            long time = currTime.getTime() - lastTime.getTime();
            if (StringUtils.equals("offline", lastStatus) && StringUtils.equals("online", currStatus)) {
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

        long firstSecond = firstTime.getTime() - startTime.getTime();
        long lastSecond = endTime.getTime() - lastTime.getTime();
        if (StringUtils.equals("offline", firstStatus)) {
            totalOnlineSecond += firstSecond;
            if (firstSecond < minOnlineSecond) {
                minOnlineSecond = firstSecond;
            }
            if (firstSecond > maxOnlineSecond) {
                maxOnlineSecond = firstSecond;
            }
        } else {
            totalOfflineSecond += firstSecond;
            if (firstSecond < minOfflineSecond) {
                minOfflineSecond = firstSecond;
            }
            if (firstSecond > maxOfflineSecond) {
                maxOfflineSecond = firstSecond;
            }
        }

        if (StringUtils.equals("offline", lastStatus)) {
            totalOfflineSecond += lastSecond;
            if (lastSecond < minOfflineSecond) {
                minOfflineSecond = lastSecond;
            }
            if (lastSecond > maxOfflineSecond) {
                maxOfflineSecond = lastSecond;
            }
        } else {
            totalOnlineSecond += lastSecond;
            if (lastSecond < minOnlineSecond) {
                minOnlineSecond = lastSecond;
            }
            if (lastSecond > maxOnlineSecond) {
                maxOnlineSecond = lastSecond;
            }
        }

        BigDecimal onlineRate = NumberUtil.round(Double.parseDouble(totalOnlineSecond + "") / (totalOnlineSecond + totalOfflineSecond) * 100, 2);
        long totalDay = (endTime.getTime() - startTime.getTime()) / 86400 / 1000;
        BigDecimal dayOfflineCount = NumberUtil.round(Double.parseDouble(totalOfflineCount + "") / totalDay, 2);

//        printResult(deviceName, did, totalOfflineCount, dayOfflineCount, onlineRate, totalOnlineSecond, totalOfflineSecond, totalOnlineCount,
//                maxOnlineSecond, maxOfflineSecond, minOnlineSecond, minOfflineSecond, hourCount);

        printResult2(deviceName, did, totalOfflineCount, dayOfflineCount, onlineRate, totalOnlineSecond, totalOfflineSecond, totalOnlineCount,
                maxOnlineSecond, maxOfflineSecond, minOnlineSecond, minOfflineSecond, hourCount);
    }

    private void printResult(String did, String deviceName, long totalOfflineCount, BigDecimal dayOfflineCount,
                             BigDecimal onlineRate, long totalOnlineSecond, long totalOfflineSecond,
                             long totalOnlineCount, long maxOnlineSecond, long maxOfflineSecond,
                             long minOnlineSecond, long minOfflineSecond, Map<String, Integer> hourCount) {
        long totalSecond = totalOnlineSecond + totalOfflineSecond;
        long avgOnlineSecond = 0;
        long avgOfflineSecond = 0;
        if (totalOnlineCount > 0) {
            avgOnlineSecond = totalOnlineSecond / totalOnlineCount;
        }
        if (totalOfflineCount > 0) {
            avgOfflineSecond = totalOfflineSecond / totalOfflineCount;
        }

        System.out.print("站点名称\t");
        System.out.print("DID\t");
        System.out.print("在线率\t");
        System.out.print("总离线次数\t");
        System.out.print("日均离线次数\t");
        System.out.print("统计周期\t");
        System.out.print("总在线时长\t");
        System.out.print("总离线时长\t");
        System.out.print("平均在线时长\t");
        System.out.print("平均离线时长\t");
        System.out.print("最长在线时长\t");
        System.out.print("最长离线时长\t");
        System.out.print("最短在线时长\t");
        System.out.println("最短离线时长\t");

        String totalSecondFormat = formatSecond(totalSecond);
        String totalOnlineSecondFormat = formatSecond(totalOnlineSecond);
        String totalOfflineSecondFormat = formatSecond(totalOfflineSecond);
        String avgOnlineSecondFormat = formatSecond(avgOnlineSecond);
        String avgOfflineSecondFormat = formatSecond(avgOfflineSecond);
        String maxOnlineSecondFormat = formatSecond(maxOnlineSecond);
        String maxOfflineSecondFormat = formatSecond(maxOfflineSecond);
        String minOnlineSecondFormat = formatSecond(minOnlineSecond);
        String minOfflineSecondFormat = formatSecond(minOfflineSecond);

        System.out.print(deviceName + "\t");
        System.out.print("'" + did + "\t");
        System.out.print(onlineRate + "%" + "\t");
        System.out.print(totalOfflineCount + "\t");
        System.out.print(dayOfflineCount + "\t");
        System.out.print(totalSecondFormat + "\t");
        System.out.print(totalOnlineSecondFormat + "\t");
        System.out.print(totalOfflineSecondFormat + "\t");
        if (avgOnlineSecond > 0) {
            System.out.print(avgOnlineSecondFormat + "\t");
        } else {
            System.out.print("0秒" + "\t");
        }
        if (avgOfflineSecond > 0) {
            System.out.print(avgOfflineSecondFormat + "\t");
        } else {
            System.out.print("0秒" + "\t");
        }
        System.out.print(maxOnlineSecondFormat + "\t");
        System.out.print(maxOfflineSecondFormat + "\t");
        System.out.print(minOnlineSecondFormat + "\t");
        System.out.print(minOfflineSecondFormat);
    }

    private void printResult2(String did, String deviceName, long totalOfflineCount, BigDecimal dayOfflineCount, BigDecimal onlineRate,
                              long totalOnlineSecond, long totalOfflineSecond,
                              long totalOnlineCount, long maxOnlineSecond, long maxOfflineSecond,
                              long minOnlineSecond, long minOfflineSecond, Map<String, Integer> hourCount) {
        long totalSecond = totalOnlineSecond + totalOfflineSecond;
        long avgOnlineSecond = 0;
        long avgOfflineSecond = 0;
        if (totalOnlineCount > 0) {
            avgOnlineSecond = totalOnlineSecond / totalOnlineCount;
        }
        if (totalOfflineCount > 0) {
            avgOfflineSecond = totalOfflineSecond / totalOfflineCount;
        }

        String totalSecondFormat = formatSecond(totalSecond);
        String totalOnlineSecondFormat = formatSecond(totalOnlineSecond);
        String totalOfflineSecondFormat = formatSecond(totalOfflineSecond);
        String avgOnlineSecondFormat = formatSecond(avgOnlineSecond);
        String avgOfflineSecondFormat = formatSecond(avgOfflineSecond);
        String maxOnlineSecondFormat = formatSecond(maxOnlineSecond);
        String maxOfflineSecondFormat = formatSecond(maxOfflineSecond);
        String minOnlineSecondFormat = formatSecond(minOnlineSecond);
        String minOfflineSecondFormat = formatSecond(minOfflineSecond);

        System.out.println("在线率\t" + onlineRate + "%");
        System.out.println("总在线次数\t" + totalOnlineCount + "次");
        System.out.println("总离线次数\t" + totalOfflineCount + "次");
        System.out.println("日均离线次数\t" + dayOfflineCount + "次");
        System.out.println("统计周期\t" + totalSecondFormat);
        System.out.println("总在线时长\t" + totalOnlineSecondFormat);
        System.out.println("总离线时长\t" + totalOfflineSecondFormat);
        if (avgOnlineSecond > 0) {
            System.out.println("平均在线时长\t" + avgOnlineSecondFormat);
        } else {
            System.out.println("平均在线时长\t" + "0秒" + "\t");
        }
        if (avgOfflineSecond > 0) {
            System.out.println("平均离线时长\t" + avgOfflineSecondFormat);
        } else {
            System.out.println("平均离线时长\t" + "0秒" + "\t");
        }
        System.out.println("最长在线时长\t" + maxOnlineSecondFormat);
        System.out.println("最长离线时长\t" + maxOfflineSecondFormat);
        System.out.println("最短在线时长\t" + minOnlineSecondFormat);
        System.out.println("最短离线时长\t" + minOfflineSecondFormat);
        System.out.println(hourCount);
        for (String hour : hourCount.keySet()) {
//            System.out.println(hour + "点\t" + hourCount.get(hour));
        }
    }

    private String formatSecond(long second) {
        if (second < 1000) {
            return DateUtil.formatBetween(second);
        }
        return DateUtil.formatBetween(second, BetweenFormatter.Level.SECOND);
    }
}
