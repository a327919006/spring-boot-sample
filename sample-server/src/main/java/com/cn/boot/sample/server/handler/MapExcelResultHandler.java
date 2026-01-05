package com.cn.boot.sample.server.handler;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.write.metadata.WriteSheet;
import com.alibaba.excel.write.style.column.LongestMatchColumnWidthStyleStrategy;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.session.ResultContext;
import org.apache.ibatis.session.ResultHandler;

import java.io.OutputStream;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
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
    private List<String> headers;
    private boolean headersInitialized = false;

    public MapExcelResultHandler(OutputStream outputStream, int bufferSize) {
        this.bufferSize = bufferSize;
        this.buffer = new ArrayList<>(bufferSize);
        this.count = new AtomicInteger(0);
        this.outputStream = outputStream;
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

        // 停止条件（可选）
        if (shouldStop(resultContext)) {
            resultContext.stop();
        }
    }

    private void initializeHeaders(Map<String, Object> firstRecord) {
        this.headers = new ArrayList<>(firstRecord.keySet());

        // 创建 ExcelWriter，使用动态表头
        this.excelWriter = EasyExcel.write(outputStream)
                .head(generateHead())
                .autoCloseStream(false)
                .registerWriteHandler(new LongestMatchColumnWidthStyleStrategy())
                .build();

        this.writeSheet = EasyExcel.writerSheet("数据导出").build();
        this.headersInitialized = true;

        log.info("初始化表头: {}", headers);
    }

    private List<List<String>> generateHead() {
        List<List<String>> head = new ArrayList<>();
        for (String header : headers) {
            List<String> headColumn = new ArrayList<>();
            headColumn.add(header);
            head.add(headColumn);
        }
        return head;
    }

    private List<List<Object>> convertBufferToExcelData() {
        List<List<Object>> excelData = new ArrayList<>();

        for (Map<String, Object> data : buffer) {
            List<Object> row = new ArrayList<>();
            for (String header : headers) {
                Object value = data.get(header);
                if (value instanceof Timestamp) {
                    value = value.toString();
                } else if (value instanceof Date) {
                    value = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format((Date) value);
                }
                row.add(value);
            }
            excelData.add(row);
        }

        return excelData;
    }

    private void flushToExcel() {
        if (!buffer.isEmpty() && excelWriter != null) {
            List<List<Object>> excelData = convertBufferToExcelData();
            excelWriter.write(excelData, writeSheet);
            buffer.clear();
            log.debug("已写入 {} 条数据到Excel", excelData.size());
        }
    }

    private boolean shouldStop(ResultContext<? extends Map<String, Object>> resultContext) {
        // 自定义停止逻辑
        // return count.get() >= 1000000; // 限制最大记录数
        return false;
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

    public List<String> getHeaders() {
        return headers;
    }
}