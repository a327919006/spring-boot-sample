package com.cn.boot.sample.agent;

import java.lang.instrument.Instrumentation;

/**
 * @author Chen Nan
 */
public class MyAgent {
    // premain是javaagent的入口
    public static void premain(String agentArgs, Instrumentation instrumentation) {
        System.out.println("我是" + agentArgs);
    }
}
