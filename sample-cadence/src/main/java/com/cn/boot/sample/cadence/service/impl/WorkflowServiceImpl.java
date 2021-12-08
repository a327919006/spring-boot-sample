package com.cn.boot.sample.cadence.service.impl;

import com.cn.boot.sample.cadence.service.IActivityService;
import com.cn.boot.sample.cadence.service.IWorkflowService;
import com.uber.cadence.workflow.Workflow;
import lombok.extern.slf4j.Slf4j;

/**
 * @author Chen Nan
 */
@Slf4j
public class WorkflowServiceImpl implements IWorkflowService {

    private final IActivityService activityService = Workflow.newActivityStub(IActivityService.class);

    @Override
    public String sayHello(String name) {
        log.info("sayHello name={}", name);
        String result = activityService.doHello(name);
        log.info("sayHello result={}", result);
        return result;
    }
}
