package com.cn.boot.sample.netty.server;

import com.cn.boot.sample.api.model.Constants;
import com.cn.boot.sample.netty.model.ReqDeviceData;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.ReferenceCountUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * <p>Title:</p>
 * <p>Description:</p>
 *
 * @author Chen Nan
 * @date 2017/12/27.
 */
@Slf4j
public class ServerHandler extends ChannelInboundHandlerAdapter {

    public ServerHandler() {
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        super.channelActive(ctx);
        String channelId = ctx.channel().id().asShortText();
        String ip = ClientUtils.getClientIpAddress(ctx);
        log.info("新连接, channelId={}, ip={}", channelId, ip);
    }

    @Override
    public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
        super.handlerRemoved(ctx);
        ClientUtils.removeClient(ctx);
    }

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
     * 处理设备连接
     */
    private void handleDeviceConnection(ReqDeviceData data, ChannelHandlerContext ctx) {
        // 添加设备到连接池
        ClientUtils.addClient(data.getDeviceNo(), ctx);
    }

    /**
     * 处理设备返回结果
     */
    private void handleDeviceResult(ReqDeviceData data) {
        log.info("处理业务..., data={}", data);
    }

    /**
     * 异常时释放连接
     */
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        // 当异常时释放关闭连接
        cause.printStackTrace();
        ClientUtils.removeClient(ctx);
        ctx.close();
    }
}
