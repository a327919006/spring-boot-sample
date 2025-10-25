package com.cn.boot.sample.websocket.client;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.client.WebSocketClient;
import org.springframework.web.socket.client.WebSocketConnectionManager;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

/**
 * @author Chen Nan
 */
@Slf4j
@Component
public class ClientManager {
    @Value("${websocket.server.url:ws://127.0.0.1:9072/websocket/12345}")
    private String webSocketUrl;

    @Autowired
    private WebSocketClient webSocketClient;

    @Autowired
    private MyClientHandler webSocketHandler;

    private WebSocketConnectionManager connectionManager;

    @PostConstruct
    public void connect() {
        log.info("正在连接 WebSocket 服务器: {}", webSocketUrl);

        connectionManager = new WebSocketConnectionManager(
                webSocketClient,
                webSocketHandler,
                webSocketUrl
        );

        connectionManager.start();

        // 等待连接建立
        try {
            webSocketHandler.waitForConnection();
            log.info("WebSocket 连接成功");
        } catch (InterruptedException e) {
            log.error("等待连接时被中断", e);
        }
    }

    @PreDestroy
    public void disconnect() {
        if (connectionManager != null) {
            connectionManager.stop();
            log.info("WebSocket 连接已关闭");
        }
    }

    public void sendMessage(String message) {
        try {
            webSocketHandler.sendMessage(message);
        } catch (Exception e) {
            log.error("发送消息失败", e);
        }
    }

    public boolean isConnected() {
        return webSocketHandler.isConnected();
    }
}

