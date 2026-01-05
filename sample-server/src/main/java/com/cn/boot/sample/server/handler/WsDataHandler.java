package com.cn.boot.sample.server.handler;

import org.apache.ibatis.session.ResultContext;
import org.apache.ibatis.session.ResultHandler;

import javax.websocket.Session;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * WebSocket数据处理
 *
 * @author Chen Nan
 */
public class WsDataHandler implements ResultHandler<Map<String, Object>> {
    private AtomicInteger count = new AtomicInteger(0);
    private Session session;
    private StringBuilder row;
    private List<String> identifierList;

    public WsDataHandler(Session session, List<String> identifierList, StringBuilder row) {
        this.session = session;
        this.row = row;
        this.identifierList = identifierList;
        row.ensureCapacity(1024 * 1024 * 10);
        row.append(String.join(",", identifierList)).append("\n");
    }

    @Override
    public void handleResult(ResultContext<? extends Map<String, Object>> resultContext) {
        Map<String, Object> data = resultContext.getResultObject();
        for (String identifier : identifierList) {
            Object value = data.get(identifier);
            row.append(value).append(",");
        }
        row.deleteCharAt(row.length() - 1);
        row.append("\n");

        // 数据发送给前端
        if (count.incrementAndGet() % 1000 == 0) {
            // 去掉最后一个\n
            row.setLength(row.length() - 1);
            session.getAsyncRemote().sendText(row.toString());
            row.setLength(0);
        }
    }
}
