package com.cn.boot.sample.server.handler;

import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.session.ResultContext;
import org.apache.ibatis.session.ResultHandler;

import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author Chen Nan
 */
@Slf4j
public class CsvResultHandler implements ResultHandler<Map<String, Object>> {

    private final OutputStream outputStream;
    private final CsvExportUtil csvExportUtil;
    private final List<Map<String, Object>> buffer;
    private final AtomicInteger count;
    private final int bufferSize;
    private List<String> headerList;
    private List<String> identifyList;
    private boolean headersInitialized = false;

    public CsvResultHandler(OutputStream outputStream) {
        this.outputStream = outputStream;
        this.bufferSize = 1000;
        this.buffer = new ArrayList<>(bufferSize);
        this.count = new AtomicInteger(0);
        this.csvExportUtil = new CsvExportUtil();
    }

    public CsvResultHandler(OutputStream outputStream, List<String> headerList) {
        this.outputStream = outputStream;
        this.bufferSize = 1000;
        this.headerList = headerList;
        this.identifyList = headerList;
        this.buffer = new ArrayList<>(bufferSize);
        this.count = new AtomicInteger(0);
        this.csvExportUtil = new CsvExportUtil();
    }

    public CsvResultHandler(OutputStream outputStream, List<String> headerList, List<String> identifyList) {
        this.outputStream = outputStream;
        this.bufferSize = 1000;
        this.headerList = headerList;
        this.identifyList = identifyList;
        this.buffer = new ArrayList<>(bufferSize);
        this.count = new AtomicInteger(0);
        this.csvExportUtil = new CsvExportUtil();
    }

    @Override
    public void handleResult(ResultContext<? extends Map<String, Object>> resultContext) {
        Map<String, Object> data = resultContext.getResultObject();

        // 初始化表头
        if (!headersInitialized) {
            initializeHeaders(data);
        }

        buffer.add(data);
        count.incrementAndGet();

        // 缓冲区满了就写入 CSV
        if (buffer.size() >= bufferSize) {
            flushToCsv();
        }

        // 日志记录
        if (count.get() % 10000 == 0) {
            log.info("已处理 {} 条记录", count.get());
        }
    }

    private void initializeHeaders(Map<String, Object> firstRecord) {
        if (this.headerList == null) {
            this.headerList = new ArrayList<>(firstRecord.keySet());
            this.identifyList = new ArrayList<>(firstRecord.keySet());
        }
        try {
            csvExportUtil.writeHeaders(outputStream, headerList);
            headersInitialized = true;
            log.info("初始化CSV表头: {}", headerList);
        } catch (IOException e) {
            throw new RuntimeException("写入CSV表头失败", e);
        }
    }

    private List<Object> convertToRowData(Map<String, Object> data) {
        List<Object> row = new ArrayList<>();
        for (String identify : identifyList) {
            row.add(data.get(identify));
        }
        return row;
    }

    private void flushToCsv() {
        if (!buffer.isEmpty()) {
            try {
                List<List<Object>> batchData = new ArrayList<>();
                for (Map<String, Object> data : buffer) {
                    batchData.add(convertToRowData(data));
                }
                csvExportUtil.writeBatch(outputStream, batchData);
                buffer.clear();
                log.debug("已写入 {} 条数据到CSV", batchData.size());
            } catch (IOException e) {
                throw new RuntimeException("写入CSV数据失败", e);
            }
        }
    }

    public void finish() {
        // 写入剩余数据
        if (!buffer.isEmpty()) {
            flushToCsv();
        }

        try {
            outputStream.flush();
        } catch (IOException e) {
            log.error("刷新输出流失败", e);
        }

        log.info("CSV导出完成，共处理 {} 条记录", count.get());
    }

    public int getProcessedCount() {
        return count.get();
    }
}