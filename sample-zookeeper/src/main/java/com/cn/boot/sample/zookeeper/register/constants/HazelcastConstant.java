package com.cn.boot.sample.zookeeper.register.constants;

/**
 * @author Chen Nan
 */
public class HazelcastConstant {
    private HazelcastConstant() {
        throw new IllegalStateException("Can't instance HazelcastConstant");
    }

    public static final String MAP_SERVER_CLIENT = "ServerClient";
    public static final String MAP_SERVER_CONNECT_COUNT = "ServerConnectCount";
    public static final String MAP_SERVER_CPU_RATE = "ServerCpuRate";

}
