package com.cn.boot.sample.netty.custom.coder;

import cn.hutool.core.util.ByteUtil;
import com.cn.boot.sample.netty.custom.model.CustomProtocol;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import org.apache.commons.lang3.ArrayUtils;

/**
 * @author Chen Nan
 */
public class CustomEncoder extends MessageToByteEncoder<CustomProtocol> {
    @Override
    protected void encode(ChannelHandlerContext channelHandlerContext, CustomProtocol customizeProtocol, ByteBuf byteBuf) throws Exception {
        byteBuf.writeBytes(customizeProtocol.getHeadData());
        int length = 4 + customizeProtocol.getContent().length;
        byte[] lengthBytes = ByteUtil.intToBytes(length);
        byteBuf.writeBytes(ArrayUtils.subarray(lengthBytes, 0, 2));
        byteBuf.writeBytes(customizeProtocol.getContent());
    }
}
