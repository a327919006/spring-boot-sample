package com.cn.boot.sample.business.async;

import cn.hutool.core.thread.ThreadUtil;
import com.cn.boot.sample.api.model.po.Client;
import com.cn.boot.sample.api.service.ClientService;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.Reference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;

/**
 * <p>Title:</p>
 * <p>Description:</p>
 *
 * @author Chen Nan
 * @date 2019/6/15.
 */
@Component
@Slf4j
public class ClientQueueListener implements ApplicationListener<ContextRefreshedEvent> {

    @Autowired
    private ClientQueue clientQueue;
    @Autowired
    private DeferredResultHolder resultHolder;
    @Autowired
    private ThreadPoolTaskExecutor poolTaskExecutor;
    @Reference
    private ClientService clientService;

    @Override
    public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {
        poolTaskExecutor.execute(() -> {
            while (true) {
                if (clientQueue.getClientId() != null) {
                    log.info("收到MQ消息");
                    Client client = clientService.selectByPrimaryKey(clientQueue.getClientId());
                    resultHolder.getMap().get(clientQueue.getClientId()).setResult(client);
                    log.info("处理MQ消息成功");
                    break;
                } else {
                    ThreadUtil.sleep(100);
                }
            }
        });
    }
}
