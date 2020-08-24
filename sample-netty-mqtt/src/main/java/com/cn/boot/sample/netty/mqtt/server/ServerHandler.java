package com.cn.boot.sample.netty.mqtt.server;

import com.cn.boot.sample.netty.mqtt.model.ReqDeviceData;
import com.cn.boot.sample.netty.mqtt.util.NettyClientUtils;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.codec.mqtt.*;
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
            MqttMessage req = (MqttMessage) msg;

            String channelId = ctx.channel().id().asShortText();
            String ip = ClientUtils.getClientIpAddress(ctx);
            log.info("接收数据, data={}, channelId={}, ip={}", msg, channelId, ip);

            if (req.fixedHeader().messageType().equals(MqttMessageType.CONNECT)) {
                // 收到设备连接数据
                MqttConnectMessage message = (MqttConnectMessage) msg;
                handleDeviceConnection(ctx, message);
            } else if (req.fixedHeader().messageType().equals(MqttMessageType.PINGREQ)) {
                // 收到设备心跳
                handleDevicePing(ctx);
            } else if (req.fixedHeader().messageType().equals(MqttMessageType.PUBLISH)) {
                // 收到设备处理结果数据
                MqttPublishMessage message = (MqttPublishMessage) msg;
                ByteBuf payload = message.payload();
                byte[] data = NettyClientUtils.readBytesAndRewind(payload);
//                log.info("data={}", new String(data));
                ReqDeviceData.DeviceData reqData = ReqDeviceData.DeviceData.parseFrom(data);
                log.info("data={}", reqData.getData());
                handleDevicePublish(ctx);
            } else if (req.fixedHeader().messageType().equals(MqttMessageType.PUBACK)) {
                // 收到PUBACK
                if (msg instanceof MqttPubAckMessage) {
                    MqttPubAckMessage message = (MqttPubAckMessage) msg;
                    int messageId = message.variableHeader().messageId();
                    log.info("PUBACK messageId={}", messageId);
                }
            } else {
                // 收到未知数据
                log.error("unknown data={}", req);
                ctx.close();
            }
        } finally {
            ReferenceCountUtil.release(msg);
        }
    }

    /**
     * 业务上记录设备连接
     */
    private void handleDeviceConnection(ChannelHandlerContext ctx, MqttConnectMessage message) {
        // 添加设备到连接池
        ClientUtils.addClient(ctx, message);

        MqttMessage rsp = MqttMessageFactory.newMessage(
                new MqttFixedHeader(MqttMessageType.CONNACK, false, MqttQoS.AT_MOST_ONCE, false, 0),
                new MqttConnAckVariableHeader(MqttConnectReturnCode.CONNECTION_ACCEPTED, false),
                null);
        ctx.writeAndFlush(rsp);
    }

    /**
     * 处理设备心跳
     */
    private void handleDevicePing(ChannelHandlerContext ctx) {
        MqttMessage rsp = MqttMessageFactory.newMessage(
                new MqttFixedHeader(MqttMessageType.PINGRESP, false, MqttQoS.AT_MOST_ONCE, false, 0),
                null,
                null);
        ctx.writeAndFlush(rsp);
    }

    /**
     * 处理收到设备消息
     */
    private void handleDevicePublish(ChannelHandlerContext ctx) {
        ByteBuf ackPayload = Unpooled.buffer(1);
        ackPayload.ensureWritable(1).writeByte(0);

        MqttMessage rsp = MqttMessageFactory.newMessage(
                new MqttFixedHeader(MqttMessageType.PUBACK, false, MqttQoS.AT_MOST_ONCE, false, 0),
                MqttMessageIdVariableHeader.from(1),
                ackPayload);
        ctx.writeAndFlush(rsp);
    }

    /**
     * 处理业务数据
     */
    private void handleDeviceResult(ReqDeviceData.DeviceData data) {
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
