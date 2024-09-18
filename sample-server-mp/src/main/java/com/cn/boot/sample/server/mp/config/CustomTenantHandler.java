package com.cn.boot.sample.server.mp.config;

import com.baomidou.mybatisplus.extension.plugins.handler.TenantLineHandler;
import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.expression.LongValue;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

/**
 * @author Chen Nan
 */
@Component
public class CustomTenantHandler implements TenantLineHandler {

    @Override
    public Expression getTenantId() {
        // 假设有一个租户上下文，能够从中获取当前用户的租户
        // Long tenantId = TenantContextHolder.getCurrentTenantId();
        long tenantId = 1L;
        // 返回租户ID的表达式，LongValue 是 JSQLParser 中表示 bigint 类型的 class
        return new LongValue(tenantId);
    }

    @Override
    public String getTenantIdColumn() {
        return "tenant_id";
    }

    @Override
    public boolean ignoreTable(String tableName) {
        // 根据需要返回是否忽略该表
        return !StringUtils.equals(tableName, "t_people");
    }

}