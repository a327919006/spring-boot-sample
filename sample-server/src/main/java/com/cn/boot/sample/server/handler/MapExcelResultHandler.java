package com.cn.boot.sample.server.handler;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.write.metadata.WriteSheet;
import com.alibaba.excel.write.style.column.LongestMatchColumnWidthStyleStrategy;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.session.ResultContext;
import org.apache.ibatis.session.ResultHandler;

import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author Chen Nan
 */
@Slf4j
public class MapExcelResultHandler implements ResultHandler<Map<String, Object>> {

    private ExcelWriter excelWriter;
    private WriteSheet writeSheet;
    private final List<Map<String, Object>> buffer;
    private final AtomicInteger count;
    private final int bufferSize;
    private final OutputStream outputStream;
    private final Map<String, String> customHeaders; // 自定义表头映射
    private List<String> fieldOrder; // 字段顺序
    private boolean headersInitialized = false;

    public MapExcelResultHandler(OutputStream outputStream,
                                 int bufferSize,
                                 Map<String, String> customHeaders,
                                 List<String> fieldOrder) {
        this.bufferSize = bufferSize;
        this.buffer = new ArrayList<>(bufferSize);
        this.count = new AtomicInteger(0);
        this.outputStream = outputStream;
        this.customHeaders = customHeaders != null ? customHeaders : new HashMap<>();
        this.fieldOrder = fieldOrder;
    }

    public MapExcelResultHandler(OutputStream outputStream, int bufferSize) {
        this(outputStream, bufferSize, null, null);
    }

    @Override
    public void handleResult(ResultContext<? extends Map<String, Object>> resultContext) {
        Map<String, Object> data = resultContext.getResultObject();

        // 初始化表头（使用第一条记录）
        if (!headersInitialized) {
            initializeHeaders(data);
        }

        buffer.add(data);
        count.incrementAndGet();

        // 缓冲区满了就写入 Excel
        if (buffer.size() >= bufferSize) {
            flushToExcel();
        }

        // 日志记录
        if (count.get() % 1000 == 0) {
            log.info("已处理 {} 条记录", count.get());
        }
    }

    private void initializeHeaders(Map<String, Object> firstRecord) {
        // 确定字段顺序
        if (fieldOrder == null || fieldOrder.isEmpty()) {
            fieldOrder = new ArrayList<>(firstRecord.keySet());
        }

        // 创建 ExcelWriter，使用动态表头
        this.excelWriter = EasyExcel.write(outputStream)
                .head(generateHead())
                .autoCloseStream(false)
                .registerWriteHandler(new LongestMatchColumnWidthStyleStrategy())
                .build();

        this.writeSheet = EasyExcel.writerSheet("数据导出").build();
        this.headersInitialized = true;

        log.info("初始化表头完成，字段顺序: {}", fieldOrder);
    }

    private List<List<String>> generateHead() {
        List<List<String>> head = new ArrayList<>();
        for (String field : fieldOrder) {
            List<String> headColumn = new ArrayList<>();
            String headerName = customHeaders.getOrDefault(field, field);
            headColumn.add(headerName);
            head.add(headColumn);
        }
        return head;
    }

    private List<List<Object>> convertBufferToExcelData() {
        List<List<Object>> excelData = new ArrayList<>();

        for (Map<String, Object> data : buffer) {
            List<Object> row = new ArrayList<>();
            for (String field : fieldOrder) {
                Object value = data.get(field);
                // 处理特殊类型
                row.add(convertValue(value));
            }
            excelData.add(row);
        }

        return excelData;
    }

    private Object convertValue(Object value) {
        if (value == null) {
            return "";
        }

        // 处理日期类型
        if (value instanceof Date) {
            return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format((Date) value);
        }

        // 处理 LocalDateTime
        if (value instanceof LocalDateTime) {
            return DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss").format((LocalDateTime) value);
        }

        // 处理 LocalDate
        if (value instanceof LocalDate) {
            return DateTimeFormatter.ofPattern("yyyy-MM-dd").format((LocalDate) value);
        }

        return value;
    }

    private void flushToExcel() {
        if (!buffer.isEmpty() && excelWriter != null) {
            List<List<Object>> excelData = convertBufferToExcelData();
            excelWriter.write(excelData, writeSheet);
            buffer.clear();
        }
    }

    public void finish() {
        // 写入剩余数据
        if (!buffer.isEmpty()) {
            flushToExcel();
        }

        // 关闭写入器
        if (excelWriter != null) {
            excelWriter.finish();
        }

        log.info("Excel导出完成，共处理 {} 条记录", count.get());
    }

    public int getProcessedCount() {
        return count.get();
    }
}