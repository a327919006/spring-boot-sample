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

    private static final Map<String, ChannelHandlerContext> CLIENT_MAP = new HashMap<String, ChannelHandlerContext>();

    public static ChannelHandlerContext getClient(String uid) {
        return CLIENT_MAP.get(uid);
    }

    /**
     * 往连接池加入设备
     */
    public static void addClient(String deviceNo, ChannelHandlerContext client) {
        log.info("设备" + deviceNo + "加入连接");
        CLIENT_MAP.put(deviceNo, client);
    }

    /**
     * 从连接池删除设备
     */
    public static void removeClient(String deviceNo) {
        log.info("设备" + deviceNo + "断开连接");
        CLIENT_MAP.remove(deviceNo);
    }

    /**
     * 往设备发送校验请求
     */
    public static void sendMsg(ChannelHandlerContext client, String requestNo, String deviceNo, String thirdUserId, String imgUrl) {
        log.info("往设备" + deviceNo + "发送数据");
        RspDeviceData rspDeviceData = new RspDeviceData();
        rspDeviceData.setTag(Constants.DATA_CEHCK);
        rspDeviceData.setRequestNo(requestNo);
        rspDeviceData.setDeviceNo(deviceNo);
        rspDeviceData.setThirdUserId(thirdUserId);
        rspDeviceData.setImgUrl(imgUrl);
        client.writeAndFlush(rspDeviceData);
    }

    /**
     * 获取设备ip地址
     */
    public static String getClientIpAddress(ChannelHandlerContext client) {
        return client.channel().remoteAddress().toString().substring(1);
    }
}
