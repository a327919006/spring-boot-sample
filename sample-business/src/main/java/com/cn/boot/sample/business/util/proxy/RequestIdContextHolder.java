package com.cn.boot.sample.business.util.proxy;

/**
 * 本次请求的RequestId
 * @author ChenNan
 */
public class RequestIdContextHolder {
    private static final ThreadLocal<String> CONTEXT_HOLDER = new ThreadLocal<>();

    /**
     * 设置值
     */
    public static void setRequestId(String key) {
        CONTEXT_HOLDER.set(key);
    }

    /**
     * 获取值
     */
    public static String getRequestId() {
        return CONTEXT_HOLDER.get();
    }

    /**
     * 重置值
     */
    public static void clearRequestId() {
        CONTEXT_HOLDER.remove();
    }
}
