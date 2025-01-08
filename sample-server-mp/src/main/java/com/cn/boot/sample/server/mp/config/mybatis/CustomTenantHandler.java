package com.cn.boot.sample.server.mp.config.mybatis;

import com.baomidou.mybatisplus.extension.plugins.handler.TenantLineHandler;
import com.cn.boot.sample.server.mp.config.aop.TenantIdContextHolder;
import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.expression.LongValue;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

/**
 * 多租户处理器，提供租户ID给MP的sql拦截器，实现数据权限过滤
 *
 * @author Chen Nan
 */
@Component
public class CustomTenantHandler implements TenantLineHandler {

    @Override
    public Expression getTenantId() {
        // 从上下文获取租户ID
        Long tenantId = TenantIdContextHolder.getTenantId();
        // 为空时查询所有，使用场景：超级管理员账号可查看所有租户数据
        if (tenantId == null) {
            return null;
        }
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