package com.cn.boot.sample.agent;

import java.lang.instrument.Instrumentation;

/**
 * @author Chen Nan
 */
public class SampleAgent {

    public static void premain(String agentArgs, Instrumentation inst) {
        System.out.println("====premain 方法执行 agentArgs=" + agentArgs);
        inst.addTransformer(new MyTransformer());
    }
}
