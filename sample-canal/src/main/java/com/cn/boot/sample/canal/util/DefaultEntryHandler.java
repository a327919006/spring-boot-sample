package com.cn.boot.sample.canal.util;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import top.javatool.canal.client.annotation.CanalTable;
import top.javatool.canal.client.handler.EntryHandler;

import java.util.Map;

/**
 * 支持统一的处理@CanalTable(value="all"),这样除去存在EntryHandler的表以外，
 * 其他所有表的处理将通过该处理器,统一转为Map<String, String>对象
 *
 * @author Chen Nan
 */
@Slf4j
@CanalTable(value = "all")
@Component
public class DefaultEntryHandler implements EntryHandler<Map<String, String>> {
    @Override
    public void insert(Map<String, String> map) {
        log.info("insert message  {}", map);
    }

    @Override
    public void update(Map<String, String> before, Map<String, String> after) {
        log.info("update before {} ", before);
        log.info("update after {}", after);
    }

    @Override
    public void delete(Map<String, String> map) {
        log.info("delete  {}", map);
    }
}