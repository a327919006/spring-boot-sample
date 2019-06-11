package com.cn.boot.sample.amqp;

import com.cn.boot.sample.amqp.boot.test13.RabbitSender;
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

    @Autowired
    private RabbitSender rabbitSender;

    @Test
    public void test13() {
        rabbitSender.sendMessage("Hello RabbitMQ! SpringBoot!");
        rabbitSender.sendMessage("Hello RabbitMQ! SpringBoot!error");
    }
}
