package com.cn.boot.sample.business.excel;

import com.opencsv.bean.CsvBindByName;
import com.opencsv.bean.CsvDate;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Date;

/**
 * @author Chen Nan
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class ClientCsv implements Serializable {

    @CsvBindByName(column = "ID")
    private String id;

    @CsvBindByName(column = "名称")
    private String name;

    @CsvBindByName(column = "创建时间")
    @CsvDate("yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    @CsvBindByName(column = "更新时间")
    @CsvDate("yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updateTime;
}