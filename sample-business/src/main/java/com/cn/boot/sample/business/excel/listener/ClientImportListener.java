package com.cn.boot.sample.business.excel.listener;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.cn.boot.sample.business.excel.ClientExcel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;

/**
 * @author ChenNan
 */
@Slf4j
@Data
@EqualsAndHashCode(callSuper = true)
public class ClientImportListener extends AnalysisEventListener<ClientExcel> {

    /**
     * 默认每隔3000条存储数据库
     */
    private int batchCount = 3000;
    /**
     * 缓存的数据列表
     */
    private List<ClientExcel> list = new ArrayList<>();

    @Override
    public void invoke(ClientExcel data, AnalysisContext context) {
        log.info("readLine:{}", data);
        list.add(data);
        // 达到BATCH_COUNT，则调用importer方法入库，防止数据几万条数据在内存，容易OOM
        if (list.size() >= batchCount) {
            // 可以在此次执行存储到数据库的操作
            // 存储完成清理list
            list.clear();
        }
    }

    @Override
    public void doAfterAllAnalysed(AnalysisContext analysisContext) {
        log.info("readFinish:{}", list);
        // 可以在此次执行存储到数据库的操作
        // 存储完成清理list
        list.clear();
    }

}
