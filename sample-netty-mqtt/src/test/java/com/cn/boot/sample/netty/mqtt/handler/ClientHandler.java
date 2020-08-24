package com.cn.boot.sample.netty.mqtt.handler;

import com.cn.boot.sample.netty.mqtt.model.ReqDeviceData;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.codec.mqtt.*;
import io.netty.util.ReferenceCountUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * @author Chen Nan
 * @date 2017/12/27.
 */
@Slf4j
public class ClientHandler extends ChannelInboundHandlerAdapter {
    private static String deviceId = "d002";

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        MqttMessage conn = MqttMessageFactory.newMessage(
                new MqttFixedHeader(MqttMessageType.CONNECT, false, MqttQoS.AT_MOST_ONCE, false, 0),
                new MqttConnectVariableHeader("MQTT", 4, true, true, false, 0, false, true, 180),
                new MqttConnectPayload(deviceId, "", null, "77", "123".getBytes()));

        ctx.writeAndFlush(conn);
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        try {
            if (msg instanceof MqttConnAckMessage) {
                MqttConnAckMessage message = (MqttConnAckMessage) msg;
                MqttConnectReturnCode mqttConnectReturnCode = message.variableHeader().connectReturnCode();
                log.info("connAck code={}", mqttConnectReturnCode);
                if (MqttConnectReturnCode.CONNECTION_ACCEPTED.equals(mqttConnectReturnCode)) {
                    log.info("connect success");
                    publish(ctx);
                }
            } else if (msg instanceof MqttPubAckMessage) {
                MqttPubAckMessage message = (MqttPubAckMessage) msg;
                int messageId = message.variableHeader().messageId();
                log.info("pubAck messageId={}", messageId);
            }
        } finally {
            ReferenceCountUtil.release(msg);
        }
    }

    private void publish(ChannelHandlerContext ctx) {
        ReqDeviceData.DeviceData reqDeviceData = ReqDeviceData.DeviceData.newBuilder()
                .setTag("hello")
                .setDeviceNo(deviceId)
                .setData("1111")
                .build();

        byte[] bytes = reqDeviceData.toByteArray();

        ByteBuf payload = Unpooled.buffer();
        payload.ensureWritable(bytes.length).writeBytes(bytes);

        MqttMessage conn = MqttMessageFactory.newMessage(
                new MqttFixedHeader(MqttMessageType.PUBLISH, false, MqttQoS.AT_LEAST_ONCE, false, 0),
                new MqttPublishVariableHeader("MS", 1),
                payload);

        ctx.writeAndFlush(conn);
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
