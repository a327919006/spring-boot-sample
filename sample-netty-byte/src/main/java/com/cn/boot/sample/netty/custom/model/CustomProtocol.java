package com.cn.boot.sample.netty.custom.model;

import com.cn.boot.sample.netty.custom.constant.ProtocolConstant;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Chen Nan
 */
@Data
@NoArgsConstructor
public class CustomProtocol {
    private byte[] headData = ProtocolConstant.HEAD_DATA;
    private byte[] contentLength;
    private byte[] content;

    public CustomProtocol(byte[] length, byte[] data) {
        this.contentLength = length;
        this.content = data;
    }
}
