package com.cn.boot.sample.rocketmq.original.test9.transaction;

import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.producer.LocalTransactionState;
import org.apache.rocketmq.client.producer.TransactionListener;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.common.message.MessageExt;

/**
 * 事务消息业务逻辑处理
 * @author Chen Nan
 */
@Slf4j
public class TransactionListenerImpl implements TransactionListener {
    @Override
    public LocalTransactionState executeLocalTransaction(Message msg, Object arg) {
        log.info("[executeLocalTransaction]arg={}, msg={}", arg, msg);
        // 可执行具体的业务逻辑，如：
        // tx.begin
        // 数据库操作
        // tx.commit
        return LocalTransactionState.COMMIT_MESSAGE;

        // 如果返回的是UNKNOW，一分钟后会回调checkLocalTransaction方法
//        return LocalTransactionState.UNKNOW;
    }

    @Override
    public LocalTransactionState checkLocalTransaction(MessageExt msg) {
        log.info("[checkLocalTransaction]msg={}", msg);
        return LocalTransactionState.COMMIT_MESSAGE;
    }
}
