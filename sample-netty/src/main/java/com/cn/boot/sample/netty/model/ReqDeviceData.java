package com.cn.boot.sample.netty.model;

import lombok.Data;

import java.io.Serializable;

/**
 * @author Chen Nan
 */
@Data
public class ReqDeviceData implements Serializable {
    private String tag;
    private String requestNo;
    private String deviceNo;
    private String thirdUserId;
    private String result;
}
