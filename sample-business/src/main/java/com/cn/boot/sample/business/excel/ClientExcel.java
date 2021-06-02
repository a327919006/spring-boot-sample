package com.cn.boot.sample.business.excel;

import com.alibaba.excel.annotation.ExcelIgnore;
import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import com.alibaba.excel.annotation.write.style.ContentRowHeight;
import com.alibaba.excel.annotation.write.style.HeadRowHeight;
import com.cn.boot.sample.business.excel.converter.LocalDateTimeConverter;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @author Chen Nan
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@ColumnWidth(25)
@HeadRowHeight(20)
@ContentRowHeight(18)
public class ClientExcel implements Serializable {

    @ExcelIgnore
    @ExcelProperty("id")
    private String id;

    @ExcelProperty("商户名称")
    private String name;

    @ColumnWidth(30)
    @ExcelProperty(value = "创建时间", converter = LocalDateTimeConverter.class)
    private LocalDateTime createTime;
}