package com.cn.boot.sample.websocket.service;

import javax.websocket.Session;

/**
 * @author Chen Nan
 */
public interface MsgService {

    void process(Session session, String message);
}
