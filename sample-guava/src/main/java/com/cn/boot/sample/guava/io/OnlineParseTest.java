package com.cn.boot.sample.guava.io;

import cn.hutool.core.date.DateUtil;
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
        Date lastTime = null;
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
            if (lastStatus == null) {
                lastStatus = currStatus;
                lastTime = currTime;
                continue;
            }
            long time = currTime.getTime() - lastTime.getTime();
            String formatTime = DateUtil.formatBetween(time);
            if (StringUtils.equals("offline", lastStatus) && StringUtils.equals("online", currStatus)) {
                System.out.println("'" + DateUtil.format(lastTime, format) + "\t" + "'" + DateUtil.format(currTime, format) + "\t离线时长\t" + formatTime);
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
                System.out.println("'" + DateUtil.format(lastTime, format) + "\t" + "'" + DateUtil.format(currTime, format) + "\t在线\t" + formatTime);

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
        System.out.println("总上下线次数\t" + totalOfflineCount + "次");
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
            System.out.println(hour + "点\t" + hourCount.get(hour) + "次");
        }
    }
}
