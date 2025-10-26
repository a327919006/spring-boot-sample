package com.cn.boot.sample.websocket.service.impl;

import com.cn.boot.sample.websocket.service.MsgService;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;

import javax.websocket.Session;

/**
 * @author Chen Nan
 */
@Service
public class MsgServiceImpl implements MsgService {
    @SneakyThrows
    @Override
    public void process(Session session, String message) {
        session.getBasicRemote().sendText("hello " + message);
    }
}
