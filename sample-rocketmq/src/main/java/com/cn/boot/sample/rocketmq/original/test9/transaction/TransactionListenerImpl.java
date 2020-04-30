package com.cn.boot.sample.rocketmq.original.test9.transaction;

import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.producer.LocalTransactionState;
import org.apache.rocketmq.client.producer.TransactionListener;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.common.message.MessageExt;

/**
 * 事务消息业务逻辑处理
 *
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
        String str = (String) arg;
        if (str.endsWith("1")) {
            // 如果业务抛出异常，与下方UNKNOW处理方式相同
            throw new RuntimeException("222222");
        }
        // 业务处理成功则返回COMMIT_MESSAGE
        // 如果明确业务失败，可以返回ROLLBACK_MESSAGE或重试业务
        return LocalTransactionState.COMMIT_MESSAGE;

        // 如果返回的是UNKNOW，一分钟后会回调checkLocalTransaction方法
        // 举例：业务操作调用了一个第三方的http接口，由于网络波动超时了，不清楚第三方是处理成功还是失败时，返回UNKNOW
        // 第三方提供业务处理结果的查询接口，checkLocalTransaction调用第三方查询接口进行确认
//        return LocalTransactionState.UNKNOW;
    }

    @Override
    public LocalTransactionState checkLocalTransaction(MessageExt msg) {
        log.info("[checkLocalTransaction]msg={}", msg);

        // 此处执行业务确认操作，确认executeLocalTransaction方法中的业务是否执行成功
        // 成功则返回COMMIT_MESSAGE，失败则返回ROLLBACK_MESSAGE
        return LocalTransactionState.COMMIT_MESSAGE;
    }
}
