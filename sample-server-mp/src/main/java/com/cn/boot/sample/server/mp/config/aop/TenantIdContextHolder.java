package com.cn.boot.sample.server.mp.config.aop;

/**
 * 本次请求的TenantId
 *
 * @author ChenNan
 */
public class TenantIdContextHolder {
    private static final ThreadLocal<Long> CONTEXT_HOLDER = new ThreadLocal<>();

    /**
     * 设置值
     */
    public static void setTenantId(Long key) {
        CONTEXT_HOLDER.set(key);
    }

    /**
     * 获取值
     */
    public static Long getTenantId() {
        return CONTEXT_HOLDER.get();
    }

    /**
     * 重置值
     */
    public static void clearTenantId() {
        CONTEXT_HOLDER.remove();
    }
}
