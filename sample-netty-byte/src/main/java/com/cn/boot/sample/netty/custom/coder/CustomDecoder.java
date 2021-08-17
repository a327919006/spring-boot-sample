package com.cn.boot.sample.netty.custom.coder;

import cn.hutool.core.util.ByteUtil;
import com.cn.boot.sample.netty.custom.constant.ProtocolConstant;
import com.cn.boot.sample.netty.custom.model.CustomProtocol;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import org.apache.commons.lang3.ArrayUtils;

import java.util.Arrays;
import java.util.List;


/**
 * @author Chen Nan
 */
public class CustomDecoder extends ByteToMessageDecoder {

    @Override
    protected void decode(ChannelHandlerContext channelHandlerContext, ByteBuf byteBuf, List<Object> list) throws Exception {
        int total = byteBuf.readableBytes();
        if (total < ProtocolConstant.BASE_LENGTH || total > ProtocolConstant.MAX_LENGTH) {
            return;
        }
        // 记录包头开始的index
        int beginReader;

        while (true) {
            // 获取包头开始的index
            beginReader = byteBuf.readerIndex();
            // 标记包头开始的index
            byteBuf.markReaderIndex();
            // 读到了协议的开始标志，结束while循环
            byte[] headData = new byte[2];
            byteBuf.readBytes(2).getBytes(0, headData);
            if (Arrays.equals(headData, ProtocolConstant.HEAD_DATA)) {
                break;
            }

            // 未读到包头，略过一个字节
            // 每次略过，一个字节，去读取，包头信息的开始标记
            byteBuf.resetReaderIndex();
            byteBuf.readByte();

            // 当略过，一个字节之后，
            // 数据包的长度，又变得不满足
            // 此时，应该结束。等待后面的数据到达
            if (byteBuf.readableBytes() < ProtocolConstant.BASE_LENGTH) {
                return;
            }
        }

        // 消息的长度
        byte[] lengthBytes = new byte[2];
        byteBuf.readBytes(2).getBytes(0, lengthBytes);
        byte[] lengthIntBytes = ArrayUtils.add(lengthBytes, (byte) 0);
        lengthIntBytes = ArrayUtils.add(lengthIntBytes, (byte) 0);
        int length = ByteUtil.bytesToInt(lengthIntBytes);
        int contentLength = length - ProtocolConstant.BASE_LENGTH;
        // 判断请求数据包数据是否到齐
        if (byteBuf.readableBytes() < contentLength) {
            // 还原读指针
            byteBuf.readerIndex(beginReader);
            return;
        }

        // 读取data数据
        byte[] data = new byte[length - contentLength];
        byteBuf.readBytes(data);

        CustomProtocol protocol = new CustomProtocol(lengthBytes, data);
        list.add(protocol);
    }
}
