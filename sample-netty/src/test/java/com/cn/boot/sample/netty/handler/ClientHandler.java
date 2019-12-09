package com.cn.boot.sample.netty.handler;

import com.cn.boot.sample.api.model.Constants;
import com.cn.boot.sample.netty.model.ReqDeviceData;
import com.cn.boot.sample.netty.model.RspDeviceData;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.ReferenceCountUtil;

/**
 * <p>Title:</p>
 * <p>Description:</p>
 *
 * @author Chen Nan
 * @date 2017/12/27.
 */
public class ClientHandler extends ChannelInboundHandlerAdapter {
    @Override
     public void channelActive(ChannelHandlerContext ctx) throws Exception {
        ReqDeviceData reqDeviceData = new ReqDeviceData();
        reqDeviceData.setTag(Constants.DATA_CONN);
        reqDeviceData.setDeviceNo("d001");

        ctx.writeAndFlush(reqDeviceData);
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        try {
            RspDeviceData rspDeviceData = (RspDeviceData) msg;
            System.out.println("客户端接收数据：" + rspDeviceData);

            ReqDeviceData reqDeviceData = new ReqDeviceData();
            reqDeviceData.setTag(Constants.DATA_RESULT);
            reqDeviceData.setRequestNo(rspDeviceData.getRequestNo());
            reqDeviceData.setDeviceNo(rspDeviceData.getDeviceNo());
            reqDeviceData.setThirdUserId(rspDeviceData.getThirdUserId());

            reqDeviceData.setResult("1");

            ctx.writeAndFlush(reqDeviceData);
        } finally {
            ReferenceCountUtil.release(msg);
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        // 当异常时释放关闭连接
        cause.printStackTrace();
        ctx.close();
    }
}
