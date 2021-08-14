package com.cn.boot.sample.es.controller;

import com.cn.boot.sample.api.model.dto.RspBase;
import com.cn.boot.sample.es.util.ElasticsearchUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Chen Nan
 */
@Slf4j
@RestController
@RequestMapping("/import")
@Api(tags = "导入数据", produces = MediaType.APPLICATION_JSON_VALUE)
public class ImportController {

    @Autowired
    private ElasticsearchUtil elasticsearchUtil;

    @ApiOperation("通过json字符串导入单条")
    @PostMapping("/json/str")
    public RspBase<String> jsonString(String index, String json) {
        elasticsearchUtil.save(index, json);
        return RspBase.success();
    }

    @ApiOperation("通过json文件批量导入")
    @PostMapping("/json/file")
    public RspBase<Integer> jsonFile(@RequestParam MultipartFile file, @RequestParam String index) throws IOException {
        InputStream inputStream = file.getInputStream();
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
        String line;
        List<String> jsonList = new ArrayList<>();
        while ((line = reader.readLine()) != null) {
            jsonList.add(line);
        }
        elasticsearchUtil.bulkSave(index, jsonList);
        return RspBase.data(jsonList.size());
    }
}
