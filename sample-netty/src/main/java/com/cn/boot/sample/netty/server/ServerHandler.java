package com.cn.boot.sample.netty.server;

import com.cn.boot.sample.api.model.Constants;
import com.cn.boot.sample.netty.model.ReqDeviceData;
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
            ReqDeviceData data = (ReqDeviceData) msg;
            String channelId = ctx.channel().id().asShortText();
            String ip = ClientUtils.getClientIpAddress(ctx);
            log.info("接收数据, data={}, channelId={}, ip={}", data, channelId, ip);

            if (data.getTag().equals(Constants.DATA_CONN)) {
                // 收到设备连接数据
                handleDeviceConnection(data, ctx);
            } else if (data.getTag().equals(Constants.DATA_RESULT)) {
                // 收到设备处理结果数据
                handleDeviceResult(data);
            } else {
                // 收到未知数据
                log.error("unknown data={}", data);
                ctx.close();
            }
        } finally {
            ReferenceCountUtil.release(msg);
        }
    }

    /**
     * 业务上记录设备连接
     */
    private void handleDeviceConnection(ReqDeviceData data, ChannelHandlerContext ctx) {
        // 添加设备到连接池
        ClientUtils.addClient(data.getDeviceNo(), ctx);
    }

    /**
     * 处理业务数据
     */
    private void handleDeviceResult(ReqDeviceData data) {
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
