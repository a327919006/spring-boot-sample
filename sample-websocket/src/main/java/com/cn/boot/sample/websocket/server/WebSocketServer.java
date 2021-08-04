package com.cn.boot.sample.websocket.server;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author Chen Nan
 */
@Component
@Slf4j
@ServerEndpoint(value = "/test/{cid}")
public class WebSocketServer {
    private static final ConcurrentHashMap<Long, Session> SESSIONS = new ConcurrentHashMap<>();

    /**
     * 连接建立成功调用的方法
     *
     * @param cid 柜机id
     */
    @OnOpen
    public void open(@PathParam(value = "cid") Long cid, Session session) throws IOException {
        log.info("【WebSocket】（建立连接）：{}", cid);
        SESSIONS.put(cid, session);
    }

    /**
     * 连接关闭调用的方法
     *
     * @param cid 柜机id
     */
    @OnClose
    public void close(@PathParam(value = "cid") Long cid) {
        log.info("【WebSocket】（关闭连接）：{}", cid);
        SESSIONS.remove(cid);
    }

    /**
     * 发生错误时调用
     *
     * @param cid 柜机id
     */
    @OnError
    public void error(@PathParam(value = "cid") Long cid, Throwable error) {
        log.info("【WebSocket】（出现错误）：{}", cid, error);
    }

    @OnMessage
    public void message(@PathParam(value = "cid") Long cid, Session session, String message) throws IOException {
        log.info("【WebSocket】（接收消息）：{}，内容：{}", cid, message);
        session.getBasicRemote().sendText("hello" + message);
    }

    /**
     * 发送消息
     *
     * @param message 消息体
     */
    public static boolean send(Long cid, String message) throws IOException {
        log.info("【WebSocket】（发送消息）：{}，内容{}", cid, message);
        Session session = SESSIONS.get(cid);
        if (session != null) {
            session.getBasicRemote().sendText(message);
            return true;
        } else {
            return false;
        }
    }
}
