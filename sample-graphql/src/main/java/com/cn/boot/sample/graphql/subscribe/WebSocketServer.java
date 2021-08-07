package com.cn.boot.sample.graphql.subscribe;

import com.cn.boot.sample.graphql.subscribe.util.JsonKit;
import com.cn.boot.sample.graphql.subscribe.util.QueryParameters;
import graphql.ExecutionInput;
import graphql.ExecutionResult;
import graphql.GraphQL;
import lombok.extern.slf4j.Slf4j;
import org.reactivestreams.Publisher;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;
import org.springframework.stereotype.Component;

import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;


/**
 * @author Chen Nan
 */
@Slf4j
@Component
@ServerEndpoint(value = "/test")
public class WebSocketServer {

    public static GraphQL graphQL;

    public WebSocketServer() {
        log.info("=====WebSocketServer=====");
    }

    /**
     * 记录当前在线连接数
     */
    private static AtomicInteger onlineCount = new AtomicInteger(0);
    private final StockTickerGraphqlPublisher graphqlPublisher = new StockTickerGraphqlPublisher();
    private final AtomicReference<Subscription> subscriptionRef = new AtomicReference<>();

    /**
     * 连接建立成功调用的方法
     */
    @OnOpen
    public void onOpen(Session session) {
        onlineCount.incrementAndGet(); // 在线数加1
        log.info("有新连接加入：{}，当前在线人数为：{}", session.getId(), onlineCount.get());
    }

    /**
     * 连接关闭调用的方法
     */
    @OnClose
    public void onClose(Session session) {
        onlineCount.decrementAndGet(); // 在线数减1
        log.info("有一连接关闭：{}，当前在线人数为：{}", session.getId(), onlineCount.get());
        Subscription subscription = subscriptionRef.get();
        if (subscription != null) {
            subscription.cancel();
        }
    }

    /**
     * 收到客户端消息后调用的方法
     *
     * @param message 客户端发送过来的消息
     */
    @OnMessage
    public void onMessage(String message, Session session) {
        log.info("服务端收到客户端[{}]的消息:{}", session.getId(), message);
        QueryParameters parameters = QueryParameters.from(message);

        ExecutionInput executionInput = ExecutionInput.newExecutionInput()
                .query(parameters.getQuery())
                .variables(parameters.getVariables())
                .operationName(parameters.getOperationName())
                .build();

        //
        // In order to have subscriptions in graphql-java you MUST use the
        // SubscriptionExecutionStrategy strategy.
        //
//        GraphQL graphQL = GraphQL
//                .newGraphQL(graphqlPublisher.getGraphQLSchema())
//                .build();

        ExecutionResult executionResult = graphQL.execute(executionInput);

        Publisher<ExecutionResult> stockPriceStream = executionResult.getData();

        stockPriceStream.subscribe(new Subscriber<ExecutionResult>() {

            @Override
            public void onSubscribe(Subscription s) {
                subscriptionRef.set(s);
                request(1);
            }

            @Override
            public void onNext(ExecutionResult er) {
                log.debug("Sending stick price update");
                Object stockPriceUpdate = er.getData();
                sendMessage(JsonKit.toJsonString(stockPriceUpdate), session);
                request(1);
            }

            @Override
            public void onError(Throwable t) {
                log.error("Subscription threw an exception", t);
                try {
                    session.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onComplete() {
                log.info("Subscription complete");
                try {
                    session.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void request(int n) {
        Subscription subscription = subscriptionRef.get();
        if (subscription != null) {
            subscription.request(n);
        }
    }

    @OnError
    public void onError(Session session, Throwable error) {
        log.error("发生错误:", error);
    }

    /**
     * 服务端发送消息给客户端
     */
    private void sendMessage(String message, Session toSession) {
        try {
            log.info("服务端给客户端[{}]发送消息{}", toSession.getId(), message);
            toSession.getBasicRemote().sendText(message);
        } catch (Exception e) {
            log.error("服务端发送消息给客户端失败：{}", e);
        }
    }
}
