package com.cn.boot.sample.graphql.config;

import graphql.ExecutionResult;
import graphql.execution.instrumentation.InstrumentationContext;
import graphql.execution.instrumentation.InstrumentationState;
import graphql.execution.instrumentation.SimpleInstrumentation;
import graphql.execution.instrumentation.SimpleInstrumentationContext;
import graphql.execution.instrumentation.parameters.InstrumentationExecutionParameters;
import graphql.execution.instrumentation.parameters.InstrumentationFieldFetchParameters;
import graphql.schema.DataFetcher;
import org.springframework.stereotype.Component;

import java.util.concurrent.CompletableFuture;

/**
 * 类似拦截器，根据需要增加对应业务逻辑
 *
 * @author Chen Nan
 */
@Component
public class GraphqlInstrumentation extends SimpleInstrumentation {

    /**
     * 创建状态对象
     */
    @Override
    public InstrumentationState createState() {
        return new CustomInstrumentationState();
    }

    @Override
    public InstrumentationContext<ExecutionResult> beginExecution(InstrumentationExecutionParameters parameters) {
        return new SimpleInstrumentationContext<ExecutionResult>() {
            @Override
            public void onCompleted(ExecutionResult result, Throwable t) {
                CustomInstrumentationState state = parameters.getInstrumentationState();
                state.cost();
            }
        };
    }

    @Override
    public DataFetcher<?> instrumentDataFetcher(DataFetcher<?> dataFetcher, InstrumentationFieldFetchParameters parameters) {
        //
        // this allows you to intercept the data fetcher used to fetch a field and provide another one, perhaps
        // that enforces certain behaviours or has certain side effects on the data
        //
        return dataFetcher;
    }

    @Override
    public CompletableFuture<ExecutionResult> instrumentExecutionResult(ExecutionResult executionResult,
                                                                        InstrumentationExecutionParameters parameters) {
        return CompletableFuture.completedFuture(executionResult);
    }
}
