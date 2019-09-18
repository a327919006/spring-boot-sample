package com.cn.boot.sample.amqp;

import cn.hutool.core.thread.ThreadUtil;
import com.cn.boot.sample.amqp.boot.test13.RabbitSender;
import com.cn.boot.sample.amqp.boot.test14.DelayProduct;
import com.cn.boot.sample.api.model.dto.client.ClientAddReq;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * <p>Title:</p>
 * <p>Description:</p>
 *
 * @author Chen Nan
 * @date 2019/6/10.
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class BootTest {

    @Autowired(required = false)
    private RabbitSender rabbitSender;
    @Autowired(required = false)
    private DelayProduct delayProduct;

    @Test
    public void test13() {
        rabbitSender.sendMessage("Hello RabbitMQ! SpringBoot!");
        rabbitSender.sendMessage("Hello RabbitMQ! SpringBoot!error");

        ClientAddReq req = new ClientAddReq();
        req.setId("123");
        req.setName("张三");
        rabbitSender.sendMessage(req);
    }

    @Test
    public void test14(){
        delayProduct.send();

        ThreadUtil.sleep(10000);
    }
}
