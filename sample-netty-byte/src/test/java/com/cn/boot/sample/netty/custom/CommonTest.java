package com.cn.boot.sample.netty.custom;

import cn.hutool.core.util.ByteUtil;
import cn.hutool.core.util.HexUtil;
import cn.hutool.json.JSONUtil;
import org.junit.jupiter.api.Test;

/**
 * @author Chen Nan
 */
public class CommonTest {

    @Test
    public void test() {
        Integer num = 0x07E5;
        System.out.println(num);

        System.out.println(Integer.parseInt("07E5", 16));

        byte[] bytes = HexUtil.decodeHex("4E45");
        // 78,69
        System.out.println(JSONUtil.toJsonStr(bytes));

        // NE
        System.out.println(HexUtil.decodeHexStr("4E45"));

        // 4E45
        System.out.println(HexUtil.encodeHexStr("NE"));

        // a
        System.out.println(Integer.toHexString(10));

        byte[] bytes1 = ByteUtil.intToBytes(2021);
        System.out.println(JSONUtil.toJsonStr(bytes1));
        System.out.println(ByteUtil.bytesToInt(bytes1));
        System.out.println(HexUtil.encodeHexStr(bytes1));
    }
}
