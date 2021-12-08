package com.cn.boot.sample.cadence.config;

import com.cn.boot.sample.cadence.service.impl.ActivityServiceImpl;
import com.cn.boot.sample.cadence.service.impl.WorkflowServiceImpl;
import com.uber.cadence.client.WorkflowClient;
import com.uber.cadence.client.WorkflowClientOptions;
import com.uber.cadence.serviceclient.ClientOptions;
import com.uber.cadence.serviceclient.WorkflowServiceTChannel;
import com.uber.cadence.worker.Worker;
import com.uber.cadence.worker.WorkerFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author Chen Nan
 */
@Slf4j
@Configuration
public class CadenceConfig {

    public static final String DOMAIN = "sample";
    public static final String TASKLIST = "helloActivity";

    @Bean
    public WorkerFactory init() {
        // Get worker to poll the task list.
        WorkerFactory factory = WorkerFactory.newInstance(workFlowClient());
        Worker worker = factory.newWorker(TASKLIST);
        worker.registerWorkflowImplementationTypes(WorkflowServiceImpl.class);
        worker.registerActivitiesImplementations(new ActivityServiceImpl());
        factory.start();
        return factory;
    }

    @Bean
    public WorkflowClient workFlowClient(){
        log.info("init workFlowClient start");
        WorkflowClient workflowClient = WorkflowClient.newInstance(
                new WorkflowServiceTChannel(ClientOptions.newBuilder().setHost("192.168.5.130").setPort(17933).build()),
                WorkflowClientOptions.newBuilder().setDomain(DOMAIN).build());
        log.info("init workFlowClient success");
        return workflowClient;
    }
}
