package com.cn.boot.sample.websocket.controller;

import com.cn.boot.sample.api.model.dto.RspBase;
import com.cn.boot.sample.websocket.server.WebSocketServer;
import lombok.SneakyThrows;
import org.springframework.web.bind.annotation.*;

/**
 * @author Chen Nan
 */
@RestController
@RequestMapping("/server")
public class ServerController {

    @SneakyThrows
    @GetMapping("/send")
    public RspBase<String> sendMessage(@RequestParam String cid, @RequestParam String message) {
        boolean result = WebSocketServer.send(cid, message);
        return result? RspBase.success() : RspBase.fail("cid未连接");
    }

}
