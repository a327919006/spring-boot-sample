package com.cn.boot.sample.netty.mqtt.server;

import com.cn.boot.sample.api.model.Constants;
import com.cn.boot.sample.netty.mqtt.model.RspDeviceData;
import io.moquette.broker.NettyUtils;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPipeline;
import io.netty.handler.codec.mqtt.*;
import io.netty.handler.timeout.IdleStateHandler;
import io.netty.util.AttributeKey;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.Map;

/**
 * <p>Title:</p>
 * <p>Description:</p>
 *
 * @author Chen Nan
 * @date 2017/12/31.
 */
@Slf4j
public class ClientUtils {
    /**
     * 设备编号与连接
     */
    private static final Map<String, ChannelHandlerContext> CLIENT_MAP = new HashMap<>();
    /**
     * channelId与设备编号
     */
    private static final Map<String, String> DEVICE_CHANNEL_MAP = new HashMap<>();

    public static final String IDLE_STATE_HANDLER = "idleStateHandler";

    /**
     * 往连接池加入设备
     */
    public static void addClient(ChannelHandlerContext client, MqttConnectMessage message) {
        String deviceNo = message.payload().clientIdentifier();
        Channel channel = client.channel();
        String channelId = channel.id().asShortText();
        log.info("设备加入连接, deviceNo={}, channelId={}", deviceNo, channelId);
        CLIENT_MAP.put(deviceNo, client);
        DEVICE_CHANNEL_MAP.put(channelId, deviceNo);
        channel.attr(AttributeKey.valueOf("deviceNo")).set(deviceNo);

        NettyUtils.clientID(channel, deviceNo);
        int keepAlive = message.variableHeader().keepAliveTimeSeconds();
        NettyUtils.keepAlive(channel, keepAlive);
        NettyUtils.cleanSession(channel, message.variableHeader().isCleanSession());
        NettyUtils.clientID(channel, deviceNo);
        int idleTime = Math.round(keepAlive * 1.5f);
        setIdleTime(channel.pipeline(), idleTime);
    }

    /**
     * 从连接池删除设备
     */
    public static void removeClient(String deviceNo) {
        CLIENT_MAP.remove(deviceNo);
    }

    /**
     * 断开设备连接
     */
    public static boolean offline(String deviceNo) {
        log.info("offline, deviceNo={}", deviceNo);
        ChannelHandlerContext client = CLIENT_MAP.get(deviceNo);
        if (null == client) {
            log.error("设备未连接, deviceNo={}", deviceNo);
            return false;
        }
        client.close();
        return true;
    }

    /**
     * 往设备发送校验请求
     */
    public static boolean sendData(String deviceNo, String data) {
        log.info("往设备" + deviceNo + "发送数据");
        ChannelHandlerContext client = CLIENT_MAP.get(deviceNo);
        if (null == client) {
            log.error("设备未连接, deviceNo={}", deviceNo);
            return false;
        }

        RspDeviceData.DeviceData rspDeviceData = RspDeviceData.DeviceData.newBuilder()
                .setTag(Constants.DATA_CEHCK)
                .setDeviceNo(deviceNo)
                .setData(data)
                .build();

        byte[] bytes = rspDeviceData.toByteArray();// 将对象转换为byte
        int length = bytes.length;// 读取消息的长度

        ByteBuf payload = Unpooled.buffer(length);
        payload.ensureWritable(length).writeBytes(bytes);

        MqttMessage rsp = MqttMessageFactory.newMessage(
                new MqttFixedHeader(MqttMessageType.PUBLISH, false, MqttQoS.AT_MOST_ONCE, false, 0),
                new MqttPublishVariableHeader("CHECK", 0),
                payload);

        client.writeAndFlush(rsp);
        return true;
    }

    /**
     * 从连接池删除设备
     */
    public static void removeClient(ChannelHandlerContext client) {
        String channelId = client.channel().id().asShortText();
        String deviceNo = ClientUtils.getDeviceNo(channelId);
        log.info("设备断开连接, deviceNo={}, channelId={}", deviceNo, channelId);
        ClientUtils.removeClient(deviceNo);
    }

    /**
     * 根据ChannelId获取设备编号
     */
    public static String getDeviceNo(String channelId) {
        return DEVICE_CHANNEL_MAP.get(channelId);
    }

    /**
     * 获取设备ip地址
     */
    public static String getClientIpAddress(ChannelHandlerContext client) {
        return client.channel().remoteAddress().toString().substring(1);
    }

    /**
     * 设置连接状态处理器
     */
    private static void setIdleTime(ChannelPipeline pipeline, int idleTime) {
        if (pipeline.names().contains(IDLE_STATE_HANDLER)) {
            pipeline.remove(IDLE_STATE_HANDLER);
        }
        pipeline.addFirst(IDLE_STATE_HANDLER, new IdleStateHandler(idleTime, 0, 0));
    }
}
