package com.cn.boot.sample.netty.custom.constant;

/**
 * @author Chen Nan
 */
public class ProtocolConstant {
    public static byte[] HEAD_DATA = {0x4E, 0x45};
    public static final int BASE_LENGTH = 2 + 2;
    public static final int MAX_LENGTH = 5000;
}
