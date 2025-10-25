package com.cn.boot.sample.websocket.controller;

import com.cn.boot.sample.websocket.client.ClientManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author Chen Nan
 */
@RestController
@RequestMapping("/websocket")
public class WebSocketController {

    @Autowired
    private ClientManager webSocketManager;

    @GetMapping("/send")
    public String sendMessage(@RequestParam String message) {
        webSocketManager.sendMessage(message);
        return "消息已发送";
    }

    @GetMapping("/status")
    public String getStatus() {
        return webSocketManager.isConnected() ? "已连接" : "未连接";
    }

    @GetMapping("/reconnect")
    public String reconnect() {
        webSocketManager.connect();
        return "重连指令已发送";
    }
}
