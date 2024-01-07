package com.cn.boot.sample.guava.io;

import com.alibaba.fastjson.JSONArray;
import com.google.common.io.Files;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
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
public class LogParseTest {

    private File sourceFile;
    private String format = "yyyy-MM-dd HH:mm:ss.SSS";

    @BeforeAll
    public void init() throws FileNotFoundException {
        String sourceFilePath = "Explore-logs-2024-01-07 22_12_37.json";
        sourceFile = ResourceUtils.getFile("classpath:" + sourceFilePath);
    }

    /**
     * 按行处理
     */
    @Test
    public void readLines() throws IOException {
        String logStr = Files.asCharSource(sourceFile, StandardCharsets.UTF_8).read();
        List<LogLine> logLines = JSONArray.parseArray(logStr, LogLine.class);
        Map<String, LogTime> pathMap = new HashMap<>();
        for (LogLine logLine : logLines) {
            LogLine.FieldsBean fields = logLine.getFields();
            String request = fields.getRequest_uri();
            String[] split = StringUtils.split(request, " ");
            String requestUri = split[1];
            double requestTime = Double.parseDouble(fields.getRequest_time());
            String time = DateFormatUtils.format(Long.parseLong(logLine.getTimestamp().substring(0, 13)), format);
            if (!pathMap.containsKey(requestUri)) {
                pathMap.put(split[1], new LogTime(requestTime, time, 1));
            } else {
                LogTime currTime = pathMap.get(requestUri);
                int times = currTime.getTimes() + 1;
                if (requestTime < currTime.getCost()) {
                    pathMap.put(requestUri, new LogTime(requestTime, time, times));
                }
            }
        }
        // log.info("result={}", pathMap);
        Set<String> existSet = new HashSet<>();
        existSet.add("/api/bmp-charge/operationAnalysis/getMerchantPowerTrendVO");
        existSet.add("/api/bmp-ops/app/device/catalog_statistics");
        existSet.add("/api/bmp-detect-site/home/search?current=1&size=6&vehicleVinCode=014551BM3TGTS38RL");
        existSet.add("/api/bmp-charge-app/charge/data/property");
        existSet.add("/api/bmp-charge-union/charge/kuaidian/query_start_charge");
        existSet.add("/api/bmp-charge/ordercharge/export");
        existSet.add("/api/bmp-ops/repost/dbApi");
        existSet.add("/api/bmp-iot/deviceModule/callTslService");
        existSet.add("/api/bmp-ops/monitorScreen/getStationMapData?datavType=iac_station_datav&timeType=1&stationList=");
        existSet.add("/api/bmp-ops/monitorScreen/getTotalStatisticsData?datavType=ssj_station_datav&timeType=1&stationList=");
        existSet.add("/api/bmp-ops/monitorScreen/getStationMapData?datavType=ssc_station_datav&timeType=1&stationList=");
        existSet.add("/api/bmp-ops/monitorScreen/getStationMapData?datavType=ssj_station_datav&timeType=1&stationList=");

        existSet.add("/api/bmp-detect/vehicle/export");
        existSet.add("/api/bmp-charge/operationAnalysis/operationUserImageChart");
        existSet.add("/api/bmp-charge/operationAnalysis/data/property");
        existSet.add("/api/bmp-iot/deviceModule/queryConfSrv");
        existSet.add("/api/bmp-ops/station/config/list?productId=1&current=1&size=10&ascs=create_time");
        existSet.add("/api/bmp-ops/unified/deviceLifeMonitor");
        existSet.add("/api/bmp-charge/operationAnalysis/getCombinationOrderDistribution");
        existSet.add("/api/bmp-ops/thermal/unitModel");
        existSet.add("/api/bmp-ops/thermal/waterTmp");
        existSet.add("/api/bmp-charge/ordercharge/getPeriodOrderList");
        existSet.add("/api/bmp-ess-app/ipv/getHistory?chStationId=1555437114226192386&type=0&start&end");
        existSet.add("/api/bmp-charge/ordercharge/getAuthorityOrders?stationSn=350902000001");
        existSet.add("/api/bmp-charge-app/charge/vin/data/property?gunSn=3501040000050501");
        existSet.add("/api/bmp-charge-app/charge/data/scalar");
        existSet.add("/api/bmp-ess-app/station/update");
        existSet.add("/api/bmp-ops/app/device/detail");
        existSet.add("/api/bmp-ops/unified/chargeFaultReportDay");
        existSet.add("/api/bmp-ops/thermal/unitData");
        existSet.add("/api/bmp-ess-app/station/listStationByHomeId?userHomeId=1463750231733362690&selectStationId=1326030153083592706");
        existSet.add("/api/bmp-charge/operationAnalysis/operationUserImage");
        existSet.add("/api/bmp-charge/operationAnalysis/getCombinationChargeDate");
        existSet.add("/api/bmp-charge/operationAnalysis/operationSituation");

        existSet.add("/api/bmp-ops/app/afterSales/getZenTaoUsers");
        existSet.add("/api/bmp-charge-app/charge/start");
        existSet.add("/api/bmp-charge-app/charge/stop");
        existSet.add("/api/bmp-auth/ttsApp/code/send");
        existSet.add("/api/bmp-ess-app/station/listStationByHomeId");
        existSet.add("/api/bmp-ops/station/config/list");
        existSet.add("/api/bmp-ops/email/sendEmailWithHtml");
        existSet.add("/api/bmp-iot/deviceModule/pushParam");
        existSet.add("/api/bmp-charge-app/customer/amount/refund");

        pathMap.forEach((path, lineTime) -> {
            boolean exist = false;
            for (String existPath : existSet) {
                String subPath = StringUtils.substringBeforeLast(path, "?");
                String subExistPath = StringUtils.substringBeforeLast(existPath, "?");
                if (StringUtils.equals(subPath, subExistPath)) {
                    exist = true;
                    break;
                }
            }
            if (!exist) {
                System.out.println(path + "\t" + lineTime.getDatetime() + "\t" + lineTime.getCost() + "\t" + lineTime.getTimes());
            }
        });
    }
}
