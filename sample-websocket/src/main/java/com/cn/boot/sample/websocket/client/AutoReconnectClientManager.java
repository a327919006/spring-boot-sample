package com.cn.boot.sample.websocket.client;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.client.WebSocketClient;
import org.springframework.web.socket.client.WebSocketConnectionManager;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @author Chen Nan
 */
@Slf4j
//@Component
public class AutoReconnectClientManager {
    @Value("${websocket.server.url:ws://localhost:8080/websocket}")
    private String webSocketUrl;

    @Autowired
    private WebSocketClient webSocketClient;

    @Autowired
    private MyClientHandler webSocketHandler;

    private WebSocketConnectionManager connectionManager;
    private final AtomicBoolean isConnecting = new AtomicBoolean(false);
    private final AtomicBoolean shouldReconnect = new AtomicBoolean(true);

    @PostConstruct
    public void init() {
        connect();
    }

    @Async
    public void connect() {
        if (isConnecting.get() || !shouldReconnect.get()) {
            return;
        }

        isConnecting.set(true);
        try {
            log.info("正在连接 WebSocket 服务器: {}", webSocketUrl);

            if (connectionManager != null) {
                connectionManager.stop();
            }

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
                Thread.currentThread().interrupt();
                log.error("等待连接时被中断", e);
            }

        } catch (Exception e) {
            log.error("WebSocket 连接失败: {}", e.getMessage());
            scheduleReconnect();
        } finally {
            isConnecting.set(false);
        }
    }

    // 每30秒检查一次连接状态
    @Scheduled(fixedDelay = 30000)
    public void checkConnection() {
        if (shouldReconnect.get() && !webSocketHandler.isConnected() && !isConnecting.get()) {
            log.info("检测到连接断开，尝试重连...");
            connect();
        }
    }

    private void scheduleReconnect() {
        if (shouldReconnect.get()) {
            log.info("5秒后尝试重连...");
            new Thread(() -> {
                try {
                    Thread.sleep(5000);
                    if (shouldReconnect.get() && !isConnecting.get()) {
                        connect();
                    }
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }).start();
        }
    }

    @PreDestroy
    public void disconnect() {
        shouldReconnect.set(false);
        if (connectionManager != null) {
            connectionManager.stop();
            log.info("WebSocket 连接已关闭");
        }
    }

    public void sendMessage(String message) {
        if (webSocketHandler.isConnected()) {
            try {
                webSocketHandler.sendMessage(message);
            } catch (Exception e) {
                log.error("发送消息失败", e);
            }
        } else {
            log.warn("WebSocket 未连接，无法发送消息");
            // 可选：尝试重新连接
            if (!isConnecting.get()) {
                connect();
            }
        }
    }

    public boolean isConnected() {
        return webSocketHandler.isConnected();
    }
}

