package com.cn.boot.sample.graphql.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * @author Chen Nan
 */
@Data
@NoArgsConstructor
public class StockPriceUpdate {
    private String stockCode;
    private String dateTime;
    private BigDecimal stockPrice;
    private BigDecimal stockPriceChange;

    public StockPriceUpdate(String stockCode, LocalDateTime dateTime, BigDecimal stockPrice, BigDecimal stockPriceChange) {
        this.stockCode = stockCode;
        this.dateTime = dateTime.format(DateTimeFormatter.ISO_DATE_TIME);
        this.stockPrice = stockPrice;
        this.stockPriceChange = stockPriceChange;
    }
}
