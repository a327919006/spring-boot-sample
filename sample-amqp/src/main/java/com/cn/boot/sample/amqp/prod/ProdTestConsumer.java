package com.cn.boot.sample.amqp.prod;

import cn.hutool.core.thread.ThreadUtil;
import com.rabbitmq.client.Channel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * 快递柜日志消费者
 *
 * @author Xuerong Xue
 */
@Component
@Slf4j
public class ProdTestConsumer {

    @RabbitListener(queues = ProdRabbitMqConfig.PROD_TEST_QUEUE)
    public void process(String content, Channel channel, Message message) throws IOException {
        log.info("【ProdTestConsumer】开始处理：{}", content);
        ThreadUtil.sleep(100);
        log.info("【ProdTestConsumer】处理完成：{}", content);
        long deliveryTag = message.getMessageProperties().getDeliveryTag();
        channel.basicAck(deliveryTag, false);
    }
}
