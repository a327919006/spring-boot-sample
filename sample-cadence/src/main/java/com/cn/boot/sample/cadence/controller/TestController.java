package com.cn.boot.sample.cadence.controller;

import com.cn.boot.sample.api.model.dto.RspBase;
import com.cn.boot.sample.cadence.service.IWorkflowService;
import com.uber.cadence.WorkflowExecution;
import com.uber.cadence.client.WorkflowClient;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Chen Nan
 */
@Slf4j
@RestController
@RequestMapping("/test")
@Api(tags = "cadence测试", produces = MediaType.APPLICATION_JSON_VALUE)
public class TestController {

    @Autowired
    private WorkflowClient workflowClient;

    @ApiOperation("test")
    @GetMapping("test")
    public RspBase test() {
        return RspBase.success();
    }

    @ApiOperation("helloWorld")
    @GetMapping("hello")
    public RspBase helloWorld(String name) {
        IWorkflowService workflowService = workflowClient.newWorkflowStub(IWorkflowService.class);
        String result = workflowService.sayHello(name);
        return RspBase.data(result);
    }

    @ApiOperation("async")
    @GetMapping("async")
    public RspBase async(String name) {
        IWorkflowService workflowService = workflowClient.newWorkflowStub(IWorkflowService.class);
        WorkflowExecution workflowExecution = WorkflowClient.start(workflowService::sayHello, name);
        log.info("async workflowId={}, runId={}", workflowExecution.getWorkflowId(), workflowExecution.getRunId());
        return RspBase.success();
    }
}
