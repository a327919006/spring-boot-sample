package com.cn.boot.sample.business.excel.converter;

import com.alibaba.excel.converters.Converter;
import com.alibaba.excel.enums.CellDataTypeEnum;
import com.alibaba.excel.metadata.CellData;
import com.alibaba.excel.metadata.GlobalConfiguration;
import com.alibaba.excel.metadata.property.ExcelContentProperty;

/**
 * 1：启动
 * 2：已完成
 *
 * @author Chen Nan
 */
public class StatusConverter implements Converter<Integer> {
    @Override
    public Class supportJavaTypeKey() {
        return Integer.class;
    }

    @Override
    public CellDataTypeEnum supportExcelTypeKey() {
        return CellDataTypeEnum.STRING;
    }

    @Override
    public Integer convertToJavaData(CellData cellData, ExcelContentProperty contentProperty, GlobalConfiguration globalConfiguration) throws Exception {
        switch (cellData.getStringValue()) {
            case "启动":
                return 1;
            case "已完成":
                return 2;
            default:
                return -1;
        }
    }

    @Override
    public CellData<String> convertToExcelData(Integer value, ExcelContentProperty contentProperty, GlobalConfiguration globalConfiguration) throws Exception {
        switch (value) {
            case 1:
                return new CellData<>("启动");
            case 2:
                return new CellData<>("已完成");
            default:
                return new CellData<>("");
        }
    }
}
