package com.cn.boot.sample.graphql.controller;

import com.cn.boot.sample.api.model.po.User;
import graphql.ExecutionInput;
import graphql.ExecutionResult;
import graphql.GraphQL;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author Chen Nan
 */
@Slf4j
@RestController
public class GraphqlController {

    @Autowired
    private GraphQL graphQL;

    @PostMapping(value = "/sample/graphql")
    @SuppressWarnings("unchecked")
    public Object executeOperation(@RequestBody Map body) {
        String query = (String) body.get("query");
        Map<String, Object> variables = (Map<String, Object>) body.get("variables");
        if (variables == null) {
            variables = new LinkedHashMap<>();
        }

        User user = new User();
        user.setUsername("admin");
        ExecutionInput executionInput = ExecutionInput
                // 设置query语句
                .newExecutionInput(query)
                // 设置上下文
                .context(user)
                // 设置请求参数
                .variables(variables)
                .build();
        ExecutionResult executionResult = graphQL.execute(executionInput);
        Map<String, Object> result = new LinkedHashMap<>();
        if (executionResult.getErrors().size() > 0) {
            result.put("errors", executionResult.getErrors());
            log.error("Errors: {}", executionResult.getErrors());
        }
        result.put("data", executionResult.getData());
        return result;
    }
}
