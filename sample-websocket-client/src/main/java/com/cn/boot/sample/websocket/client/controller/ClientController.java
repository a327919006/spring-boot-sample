package com.cn.boot.sample.websocket.client.controller;

import com.cn.boot.sample.websocket.client.config.AutoReconnectClientManager;
import com.cn.boot.sample.websocket.client.config.ClientManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Chen Nan
 */
@RestController
@RequestMapping("/client")
public class ClientController {

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
