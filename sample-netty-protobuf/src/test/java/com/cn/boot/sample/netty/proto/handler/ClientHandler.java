package com.cn.boot.sample.netty.proto.handler;

import com.cn.boot.sample.api.model.Constants;
import com.cn.boot.sample.netty.proto.model.ReqDeviceData;
import com.cn.boot.sample.netty.proto.model.RspDeviceData;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
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
        ReqDeviceData.DeviceData reqDeviceData = ReqDeviceData.DeviceData.newBuilder()
                .setTag(Constants.DATA_CONN)
                .setDeviceNo("d001")
                .build();

        ctx.writeAndFlush(reqDeviceData);
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        try {
            RspDeviceData.DeviceData rspDeviceData = (RspDeviceData.DeviceData) msg;
            log.info("客户端接收数据：" + rspDeviceData);

            ReqDeviceData.DeviceData reqDeviceData = ReqDeviceData.DeviceData.newBuilder()
                    .setTag(Constants.DATA_RESULT)
                    .setDeviceNo(rspDeviceData.getDeviceNo())
                    .setData("ok")
                    .build();

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
