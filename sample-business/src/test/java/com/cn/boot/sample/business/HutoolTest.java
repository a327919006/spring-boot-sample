package com.cn.boot.sample.business;

import cn.hutool.crypto.Mode;
import cn.hutool.crypto.Padding;
import cn.hutool.crypto.digest.HMac;
import cn.hutool.crypto.digest.HmacAlgorithm;
import cn.hutool.crypto.symmetric.AES;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

/**
 * Hutool工具测试
 * @author Chen Nan
 */
@Slf4j
public class HutoolTest {

    @Test
    public void hmacMd5Test() {
        String data = "123456789il7B0BSEjFdzpyKzfOFpvg/Se1CP802RItKYFPfSLRxJ3jf0bVl9hvYOEktPAYW2nd7S8MBcyHYyacHKbISq5iTmDzG+ivnR+SZJv3USNTYVMz9rCQVSxd0cLlqsJauko79NnwQJbzDTyLooYoIwz75qBOH2/xOMirpeEqRJrF/EQjWekJmGk9RtboXePu2rka+Xm51syBPhiXJAq0GfbfaFu9tNqs/e2Vjja/ltE1M0lqvxfXQ6da6HrThsm5id4ClZFIi0acRfrsPLRixS/IQYtksxghvJwbqOsbIsITail9Ayy4tKcogeEZiOO+4Ed264NSKmk7l3wKwJLAFjCFogBx8GE3OBz4pqcAn/ydA=201607291424000001";

        // 此处密钥如果有非ASCII字符，考虑编码
        byte[] key = "1234567890abcdef".getBytes();
        HMac mac = new HMac(HmacAlgorithm.HmacMD5, key);

        // b977f4b13f93f549e06140971bded384
        String result = mac.digestHex(data).toUpperCase();
        log.info("result={}", result);
    }

    @Test
    public void aesTest() {
        String data = "{\"total\":1,\"stationStatusInfo\":{\"operationID\":\"123456789\",\"stationID\":\"111111111111111\",\"connectorStatusInfos\":{\"connectorID\":1,\"equipmentID\":\"10000000000000000000001\",\"status\":4,\"currentA\":0,\"currentB\":0,\"currentC\":0,\"voltageA\":0,\"voltageB\":0,\"voltageC\":0,\"soc\":10,}}}";

        byte[] secret = "1234567890abcdef".getBytes();
        byte[] iv = "1234567890abcdef".getBytes();
        AES aes = new AES(Mode.CBC, Padding.PKCS5Padding, secret, iv);

        String result = aes.encryptBase64(data);
        log.info("result={}", result);
    }
}
