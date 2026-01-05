package com.cn.boot.sample.server.service;

import com.cn.boot.sample.api.model.po.Client;
import com.cn.boot.sample.dal.mapper.ClientMapper;
import com.cn.boot.sample.server.handler.CsvResultHandler;
import com.cn.boot.sample.server.handler.MapExcelResultHandler;
import com.google.common.collect.Lists;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.session.ResultContext;
import org.apache.ibatis.session.ResultHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author Chen Nan
 */
@Slf4j
@Service
public class TestServiceImpl implements TestService {

    @Autowired
    private ClientMapper clientMapper;

    @Override
    public void listByHandler() {
        clientMapper.listByHandler(new ResultHandler<Client>() {
            @Override
            public void handleResult(ResultContext<? extends Client> resultContext) {
                Client resultObject = resultContext.getResultObject();
                log.info("resultObject: {}", resultObject);
            }
        });
    }

    @SneakyThrows
    @Override
    public void exportCsv(HttpServletResponse response) {
        List<String> headerList = Lists.newArrayList();
        headerList.add("名称");
        headerList.add("状态");
        headerList.add("创建时间");

        List<String> identifierList = Lists.newArrayList();
        identifierList.add("name");
        identifierList.add("status");
        identifierList.add("create_time");

        setupCsvResponse(response, System.currentTimeMillis() + ".csv");

        CsvResultHandler resultHandler = new CsvResultHandler(response.getOutputStream(), headerList, identifierList);
        clientMapper.export(resultHandler);

        resultHandler.finish();
    }

    @SneakyThrows
    @Override
    public void exportExcel(HttpServletResponse response) {
        setupExcelResponse(response, System.currentTimeMillis() + ".xlsx");

        // 自定义表头映射，传null为全量字段
        Map<String, String> customHeaders = new HashMap<>();
        customHeaders.put("name", "名称");
        customHeaders.put("status", "状态");
        customHeaders.put("create_time", "创建时间");

        // 字段顺序
        List<String> fieldOrder = Arrays.asList("name", "status", "create_time");

        MapExcelResultHandler resultHandler = new MapExcelResultHandler(response.getOutputStream(), 1000, customHeaders, fieldOrder);
        clientMapper.export(resultHandler);

        resultHandler.finish();
    }

    private void setupExcelResponse(HttpServletResponse response, String fileName) throws UnsupportedEncodingException {
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setCharacterEncoding("UTF-8");
        String encodedFileName = URLEncoder.encode(fileName, "UTF-8").replaceAll("\\+", "%20");
        response.setHeader("Content-Disposition", "attachment;filename=" + encodedFileName);
        response.setHeader("Access-Control-Expose-Headers", "Content-Disposition");
    }


    private void setupCsvResponse(HttpServletResponse response, String fileName) throws UnsupportedEncodingException {
        response.setContentType("text/csv; charset=UTF-8");
        response.setCharacterEncoding("UTF-8");
        String encodedFileName = URLEncoder.encode(fileName, "UTF-8").replaceAll("\\+", "%20");
        response.setHeader("Content-Disposition", "attachment;filename=" + encodedFileName);
        response.setHeader("Access-Control-Expose-Headers", "Content-Disposition");

        // 可选：设置缓冲区大小
        response.setBufferSize(8192);
    }
}
