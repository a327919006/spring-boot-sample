package com.cn.boot.sample.server.dynamic.config.dds;

/**
 * 动态数据源上下文
 *
 * @author ChenNan
 */
public class DynamicDataSourceContextHolder {

    private static final ThreadLocal<String> CONTEXT_HOLDER = new ThreadLocal<>();

    /**
     * 切换数据源key
     */
    public static void setDataSourceKey(String key) {
        CONTEXT_HOLDER.set(key);
    }

    /**
     * 获取数据源key
     */
    public static String getDataSourceKey() {
        return CONTEXT_HOLDER.get();
    }

    /**
     * 重置数据源key
     */
    public static void clearDataSourceKey() {
        CONTEXT_HOLDER.remove();
    }
}