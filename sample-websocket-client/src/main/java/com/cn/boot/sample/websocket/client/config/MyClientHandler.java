package com.cn.boot.sample.websocket.client.config;

import com.cn.boot.sample.websocket.client.service.MsgService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.util.concurrent.CountDownLatch;

/**
 * @author Chen Nan
 */
@Slf4j
@Component
public class MyClientHandler extends TextWebSocketHandler {

    private WebSocketSession session;
    private CountDownLatch connectionLatch = new CountDownLatch(1);

    @Autowired
    private MsgService msgService;

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        this.session = session;
        // 设置消息缓冲区大小（单位：字节）
        session.setBinaryMessageSizeLimit(1024 * 1024 * 10); // 1MB
        session.setTextMessageSizeLimit(1024 * 1024 * 10);   // 1MB

        log.info("WebSocket 连接已建立: {}", session.getId());
        connectionLatch.countDown();
//        try {
//            sendMessage("download");
//        } catch (Exception e) {
//            log.error("发送消息失败", e);
//        }
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        String payload = message.getPayload();
        msgService.process(payload);
    }

    @Override
    public void handleTransportError(WebSocketSession session, Throwable exception) throws Exception {
        log.error("WebSocket 传输错误: {}", exception.getMessage());
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        log.info("WebSocket 连接关闭: {}", status.getReason());
        this.session = null;
        // 可以在这里实现重连逻辑
    }

    public void sendMessage(String message) throws Exception {
        if (session != null && session.isOpen()) {
            session.sendMessage(new TextMessage(message));
        } else {
            log.warn("WebSocket 会话未就绪，无法发送消息");
        }
    }

    public boolean isConnected() {
        return session != null && session.isOpen();
    }

    public void waitForConnection() throws InterruptedException {
        connectionLatch.await();
    }
}
