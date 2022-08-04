package com.cn.boot.sample.agent;

/**
 * @author Chen Nan
 */
public class CompactDefinition {

    public void test() {
        boolean result = shouldIgnore(2);
        System.out.println(result);
    }

    private boolean shouldIgnore(int status) {
        return status == 1;
    }
}
