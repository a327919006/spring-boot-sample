package com.cn.boot.sample.cadence.service;

import com.cn.boot.sample.cadence.config.CadenceConfig;
import com.uber.cadence.workflow.WorkflowMethod;

/**
 * @author Chen Nan
 */
public interface IWorkflowService {
    @WorkflowMethod(executionStartToCloseTimeoutSeconds = 10, taskList = CadenceConfig.TASKLIST)
    String sayHello(String name);
}
