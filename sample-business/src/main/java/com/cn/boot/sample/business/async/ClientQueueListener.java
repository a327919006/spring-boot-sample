package com.cn.boot.sample.business.async;

import cn.hutool.core.thread.ThreadUtil;
import com.cn.boot.sample.api.model.po.Client;
import com.cn.boot.sample.api.service.IClientService;
import org.apache.curator.shaded.com.google.common.util.concurrent.ThreadFactoryBuilder;
import org.apache.dubbo.config.annotation.Reference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import java.util.concurrent.*;

/**
 * <p>Title:</p>
 * <p>Description:</p>
 *
 * @author Chen Nan
 * @date 2019/6/15.
 */
@Component
public class ClientQueueListener implements ApplicationListener<ContextRefreshedEvent> {

    @Autowired
    private ClientQueue clientQueue;
    @Autowired
    private DeferredResultHolder resultHolder;
    @Reference
    private IClientService clientService;

    @Override
    public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {
        ThreadFactory namedThreadFactory = new ThreadFactoryBuilder()
                .setNameFormat("demo-pool-%d").build();
        ExecutorService singleThreadPool = new ThreadPoolExecutor(1, 1,
                0L, TimeUnit.MILLISECONDS,
                new LinkedBlockingQueue<>(1024), namedThreadFactory, new ThreadPoolExecutor.AbortPolicy());

        singleThreadPool.execute(() -> {
            while (true) {
                if (clientQueue.getClientId() != null) {
                    Client client = clientService.selectByPrimaryKey(clientQueue.getClientId());
                    resultHolder.getMap().get(clientQueue.getClientId()).setResult(client);
                    clientQueue.setClientId(null);
                } else {
                    ThreadUtil.sleep(100);
                }
            }
        });
    }
}
