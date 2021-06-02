package com.cn.boot.sample.business.excel;

import com.opencsv.bean.CsvBindByName;
import com.opencsv.bean.CsvDate;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Date;

/**
 * @author Chen Nan
 */
@Data
@Accessors(chain = true)
public class ClientCsv implements Serializable {

    @CsvBindByName
    private String name;

    @CsvBindByName
    @CsvDate("yyyy-MM-dd HH:mm:ss")
    private Date createTime;
}