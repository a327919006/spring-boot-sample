package com.cn.boot.sample.netty.custom.handler;

import cn.hutool.core.util.ByteUtil;
import com.cn.boot.sample.netty.custom.model.CustomProtocol;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.ReferenceCountUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * @author Chen Nan
 * @date 2017/12/27.
 */
@Slf4j
public class ClientHandler extends ChannelInboundHandlerAdapter {
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        CustomProtocol data = new CustomProtocol();
        byte[] content = ByteUtil.intToBytes(2);
        data.setContent(content);

        ctx.writeAndFlush(data);
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        try {
            CustomProtocol data = (CustomProtocol) msg;
            log.info("客户端接收数据：" + data);

            CustomProtocol reqDeviceData = new CustomProtocol();
            byte[] content = ByteUtil.intToBytes(3);
            reqDeviceData.setContent(content);

            ctx.writeAndFlush(reqDeviceData);
        } finally {
            ReferenceCountUtil.release(msg);
        }
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        super.channelInactive(ctx);
        log.info("channelInactive");
    }

    @Override
    public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
        super.handlerRemoved(ctx);
        log.info("handlerRemoved");
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        // 当异常时释放关闭连接
        log.info("exceptionCaught");
        cause.printStackTrace();
    }
}
