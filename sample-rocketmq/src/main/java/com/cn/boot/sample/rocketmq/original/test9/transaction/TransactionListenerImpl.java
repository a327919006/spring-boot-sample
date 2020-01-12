package com.cn.boot.sample.rocketmq.original.test9.transaction;

import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.producer.LocalTransactionState;
import org.apache.rocketmq.client.producer.TransactionListener;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.common.message.MessageExt;

/**
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
    }

    @Override
    public LocalTransactionState checkLocalTransaction(MessageExt msg) {
        log.info("[checkLocalTransaction]msg={}", msg);
        return LocalTransactionState.COMMIT_MESSAGE;
    }
}
