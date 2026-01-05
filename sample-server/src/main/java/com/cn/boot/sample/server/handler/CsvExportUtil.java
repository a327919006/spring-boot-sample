package com.cn.boot.sample.server.handler;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;

/**
 * @author Chen Nan
 */
@Component
@Slf4j
public class CsvExportUtil {

    private static final String CSV_DELIMITER = ",";
    private static final String LINE_SEPARATOR = "\r\n";
    private static final String ENCLOSURE = "\"";
    private static final String CHARSET = "UTF-8";
    private static final byte[] BOM = {(byte) 0xEF, (byte) 0xBB, (byte) 0xBF}; // UTF-8 BOM

    /**
     * 写入 CSV 表头
     */
    public void writeHeaders(OutputStream outputStream, List<String> headers) throws IOException {
        // 写入 UTF-8 BOM，确保 Excel 打开时中文不乱码
        outputStream.write(BOM);

        String headerLine = convertToCsvLine(headers);
        outputStream.write(headerLine.getBytes(CHARSET));
        outputStream.write(LINE_SEPARATOR.getBytes(CHARSET));
        outputStream.flush();
    }

    /**
     * 写入单行数据
     */
    public void writeRow(OutputStream outputStream, List<Object> rowData) throws IOException {
        String csvLine = convertToCsvLine(rowData);
        outputStream.write(csvLine.getBytes(CHARSET));
        outputStream.write(LINE_SEPARATOR.getBytes(CHARSET));
    }

    /**
     * 批量写入多行数据
     */
    public void writeBatch(OutputStream outputStream, List<List<Object>> batchData) throws IOException {
        StringBuilder sb = new StringBuilder();
        for (List<Object> row : batchData) {
            sb.append(convertToCsvLine(row));
            sb.append(LINE_SEPARATOR);
        }
        outputStream.write(sb.toString().getBytes(CHARSET));
        outputStream.flush();
    }

    /**
     * 将数据列表转换为 CSV 格式的一行
     */
    private String convertToCsvLine(List<?> data) {
        if (data == null || data.isEmpty()) {
            return "";
        }

        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < data.size(); i++) {
            if (i > 0) {
                sb.append(CSV_DELIMITER);
            }
            String value = convertValueToString(data.get(i));
            sb.append(escapeCsvValue(value));
        }

        return sb.toString();
    }

    /**
     * 转义 CSV 值
     */
    private String escapeCsvValue(String value) {
        if (value == null) {
            return "";
        }

        // 如果值包含分隔符、换行符或引号，需要用引号包围并转义内部引号
        if (value.contains(CSV_DELIMITER) || value.contains("\n") || value.contains("\r") || value.contains(ENCLOSURE)) {
            return ENCLOSURE + value.replace(ENCLOSURE, ENCLOSURE + ENCLOSURE) + ENCLOSURE;
        }

        return value;
    }

    /**
     * 将对象转换为字符串
     */
    private String convertValueToString(Object value) {
        if (value == null) {
            return "";
        }

        if (value instanceof Date) {
            return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format((Date) value);
        }

        if (value instanceof LocalDateTime) {
            return DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss").format((LocalDateTime) value);
        }

        if (value instanceof LocalDate) {
            return DateTimeFormatter.ofPattern("yyyy-MM-dd").format((LocalDate) value);
        }

        if (value instanceof Boolean) {
            return (Boolean) value ? "是" : "否";
        }

        return value.toString();
    }
}