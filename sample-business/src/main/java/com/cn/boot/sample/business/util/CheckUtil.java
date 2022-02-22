package com.cn.boot.sample.business.util;

import cn.hutool.crypto.Mode;
import cn.hutool.crypto.Padding;
import cn.hutool.crypto.digest.HMac;
import cn.hutool.crypto.digest.HmacAlgorithm;
import cn.hutool.crypto.symmetric.AES;
import com.cn.boot.sample.api.enums.ErrorCode;
import com.cn.boot.sample.api.exceptions.BusinessException;
import com.cn.boot.sample.api.model.dto.ReqDTO;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

/**
 * @author Chen Nan
 */
@Slf4j
public class CheckUtil {
    private static byte[] secret = "1234567890abcdef".getBytes();

    /**
     * 数据加密
     *
     * @param param 请求参数，json串
     * @return 加密后的请求参数
     */
    public static String aesEncrypt(String param) {
        AES aes = new AES(Mode.CBC, Padding.PKCS5Padding, secret, secret);

        String result = aes.encryptBase64(param);
        log.info("result={}", result);
        return result;
    }

    /**
     * 数据加密
     *
     * @param param 请求参数，json串
     * @return 加密后的请求参数
     */
    public static String aesDecrypt(String param) {
        try {
            AES aes = new AES(Mode.CBC, Padding.PKCS5Padding, secret, secret);

            String result = aes.decryptStr(param);
            log.info("result={}", result);
            return result;
        } catch (Exception e) {
            throw new BusinessException(ErrorCode.ERROR_PARAM_BUSINESS);
        }
    }

    /**
     * 数据签名
     *
     * @param userId 用户ID
     * @param param      加密后的请求参数
     * @param timestamp  时间戳
     * @return 数据签名
     */
    public static String sign(String userId, String param, String timestamp) {
        String data = userId + param + timestamp;

        HMac mac = new HMac(HmacAlgorithm.HmacMD5, secret);

        String result = mac.digestHex(data).toUpperCase();
        log.info("result={}", result);
        return result;
    }

    /**
     * 签名校验
     *
     * @param reqDTO 请求参数
     * @return true校验通过 false校验不通过
     */
    public static boolean checkSign(ReqDTO reqDTO) {
        String sign = sign(reqDTO.getUserId(), (String) reqDTO.getData(), reqDTO.getTimeStamp());
        return StringUtils.equals(sign, reqDTO.getSig());
    }
}
