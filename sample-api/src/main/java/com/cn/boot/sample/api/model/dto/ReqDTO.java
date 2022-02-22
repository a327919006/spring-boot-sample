package com.cn.boot.sample.api.model.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * 互联互通基础请求数据
 *
 * @author Chen Nan
 */
@Data
public class ReqDTO<T> implements Serializable {
    // 用户ID
    private String userId;
    // 时间戳
    private String timeStamp;
    // 数据签名
    private String sig;
    // 签名校验结果
    private Boolean checkSig;
    // 请求参数
    private T data;
}
