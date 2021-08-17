package com.cn.boot.sample.netty.custom.server;

import cn.hutool.core.util.ByteUtil;
import cn.hutool.core.util.HexUtil;
import com.cn.boot.sample.netty.custom.model.CustomProtocol;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.AttributeKey;
import io.netty.util.ReferenceCountUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * @author Chen Nan
 * @date 2017/12/27.
 */
@Slf4j
public class ServerHandler extends ChannelInboundHandlerAdapter {

    public ServerHandler() {
    }

    /**
     * 处理客户端连接
     */
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        super.channelActive(ctx);
        String channelId = ctx.channel().id().asShortText();
        String ip = ClientUtils.getClientIpAddress(ctx);
        log.info("新连接, channelId={}, ip={}", channelId, ip);
    }

    /**
     * 处理接收到客户端发送的数据
     */
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        try {
            CustomProtocol data = (CustomProtocol) msg;
            String channelId = ctx.channel().id().asShortText();
            String ip = ClientUtils.getClientIpAddress(ctx);
            log.info("接收数据, data={}, channelId={}, ip={}", data, channelId, ip);

            CustomProtocol rsp = new CustomProtocol();
            byte[] content = ByteUtil.intToBytes(123456);
            rsp.setContent(content);
            ctx.writeAndFlush(rsp);
        } finally {
            ReferenceCountUtil.release(msg);
        }
    }

    /**
     * 业务上记录设备连接
     */
    private void handleDeviceConnection(CustomProtocol data, ChannelHandlerContext ctx) {
        // 添加设备到连接池
//        ClientUtils.addClient(data.getDeviceNo(), ctx);
    }

    /**
     * 处理业务数据
     */
    private void handleDeviceResult(CustomProtocol data) {
        log.info("处理业务..., data={}", data);
    }

    @Override
    public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
        super.handlerRemoved(ctx);
        String deviceNo = (String) ctx.channel().attr(AttributeKey.valueOf("deviceNo")).get();
        log.info("handlerRemoved deviceNo={}", deviceNo);
        ClientUtils.removeClient(ctx);
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        super.channelInactive(ctx);
        String deviceNo = (String) ctx.channel().attr(AttributeKey.valueOf("deviceNo")).get();
        log.info("channelInactive deviceNo={}", deviceNo);
    }

    /**
     * 异常时释放连接
     */
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        // 当异常时释放关闭连接
        String deviceNo = (String) ctx.channel().attr(AttributeKey.valueOf("deviceNo")).get();
        log.info("exceptionCaught deviceNo={}", deviceNo);
        cause.printStackTrace();
        ClientUtils.removeClient(ctx);
        ctx.close();
    }
}
