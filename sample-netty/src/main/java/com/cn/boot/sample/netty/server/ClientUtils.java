package com.cn.boot.sample.netty.server;

import com.cn.boot.sample.api.model.Constants;
import com.cn.boot.sample.netty.model.RspDeviceData;
import io.netty.channel.ChannelHandlerContext;
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

    /**
     * 往连接池加入设备
     */
    public static void addClient(String deviceNo, ChannelHandlerContext client) {
        String channelId = client.channel().id().asShortText();
        log.info("设备加入连接, deviceNo={}, channelId={}", deviceNo, channelId);
        CLIENT_MAP.put(deviceNo, client);
        DEVICE_CHANNEL_MAP.put(channelId, deviceNo);
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
        RspDeviceData rspDeviceData = new RspDeviceData();
        rspDeviceData.setTag(Constants.DATA_CEHCK);
        rspDeviceData.setDeviceNo(deviceNo);
        rspDeviceData.setData(data);
        client.writeAndFlush(rspDeviceData);
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
}
