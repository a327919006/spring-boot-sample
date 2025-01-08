package com.cn.boot.sample.guava.io;

import cn.hutool.core.date.BetweenFormatter;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.NumberUtil;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
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
public class ModuleConfigParseTest {

    private File sourceFile;

    @BeforeAll
    public void init() throws FileNotFoundException {
        String sourceFilePath = "module_config.json";
        sourceFile = ResourceUtils.getFile("classpath:" + sourceFilePath);
    }

    /**
     * 按行处理
     */
    @Test
    public void readLines() throws IOException {
        String logStr = Files.asCharSource(sourceFile, StandardCharsets.UTF_8).read();
        List<ModuleConfigVO> dataList = JSONArray.parseArray(logStr, ModuleConfigVO.class);

        for (ModuleConfigVO moduleConfigVO : dataList) {
//            System.out.println(moduleConfigVO);
            String configContent = moduleConfigVO.getConfigContent();
            String moduleConfig = moduleConfigVO.getModuleConfig();
            String deviceDid = moduleConfigVO.getDeviceDid();
            String deviceName = moduleConfigVO.getDeviceName();
            String moduleIdentify = moduleConfigVO.getModuleIdentify();
            String moduleNum = moduleConfigVO.getModuleNum();
            Map<String, Map<String, String>> configDictMap = parseModuleConfigMap(moduleConfig);

            if (StringUtils.isEmpty(configContent)) {
                printEmpty(deviceDid, deviceName, moduleIdentify, moduleNum);
                continue;
            }

            JSONObject groupObj = JSONObject.parseObject(configContent);
            Set<String> groupSet = groupObj.keySet();
            if (groupSet.size() == 0) {
                printEmpty(deviceDid, deviceName, moduleIdentify, moduleNum);
                continue;
            }
            for (String groupKey : groupSet) {
                JSONObject configObj = groupObj.getJSONObject(groupKey);
                Set<String> configSet = configObj.keySet();
                for (String configKey : configSet) {
                    String configValue = configObj.getString(configKey);

                    Map<String, String> groupDict = configDictMap.get(groupKey);
                    String name = "";
                    if (groupDict != null) {
                        name = groupDict.get(configKey);
                    }
                    System.out.println(
                            "'" + deviceDid + "\t" +
                                    deviceName + "\t" +
                                    moduleIdentify + "\t" +
                                    moduleNum + "\t" +
                                    groupKey + "\t" +
                                    configKey + "\t" +
                                    name + "\t" +
                                    configValue
                    );
                }
            }
        }
    }

    private void printEmpty(String deviceDid, String deviceName, String moduleIdentify, String moduleNum) {
        System.out.println(
                "'" + deviceDid + "\t" +
                        deviceName + "\t" +
                        moduleIdentify + "\t" +
                        moduleNum + "\t" +
                        "[]"
        );
    }

    private Map<String, Map<String, String>> parseModuleConfigMap(String moduleConfig) {
        Map<String, Map<String, String>> configDictMap = new HashMap<>();
        if (StringUtils.isEmpty(moduleConfig)) {
            return configDictMap;
        }

        List<ProductModuleConfigVO> productModuleConfigVOS = JSONArray.parseArray(moduleConfig, ProductModuleConfigVO.class);
        for (ProductModuleConfigVO productModuleConfigVO : productModuleConfigVOS) {
            String group = productModuleConfigVO.getGroup();
            String identifier = productModuleConfigVO.getIdentifier();
            String name = productModuleConfigVO.getName();
            Map<String, String> configMap = configDictMap.get(group);
            if (configMap == null) {
                configMap = new HashMap<>();
            }
            configMap.put(identifier, name);
            configDictMap.put(group, configMap);
        }
        return configDictMap;
    }
}
