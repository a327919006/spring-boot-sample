package com.cn.boot.sample.graphql.config.instrumentation;

import graphql.ExecutionInput;
import graphql.ExecutionResult;
import graphql.execution.ExecutionContext;
import graphql.execution.instrumentation.*;
import graphql.execution.instrumentation.parameters.*;
import graphql.language.Document;
import graphql.schema.DataFetcher;
import graphql.schema.GraphQLSchema;
import graphql.validation.ValidationError;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.CompletableFuture;

/**
 * 类似拦截器，根据需要增加对应业务逻辑
 *
 * @author Chen Nan
 */
@Slf4j
@Component
public class SampleInstrumentation extends SimpleInstrumentation {

    @Override
    public InstrumentationState createState() {
        log.info("createState");
        return super.createState();
    }

    @Override
    public InstrumentationState createState(InstrumentationCreateStateParameters parameters) {
        log.info("createState");
        return super.createState(parameters);
    }

    @Override
    public ExecutionInput instrumentExecutionInput(ExecutionInput executionInput, InstrumentationExecutionParameters parameters) {
        log.info("instrumentExecutionInput");
        return super.instrumentExecutionInput(executionInput, parameters);
    }

    @Override
    public InstrumentationContext<ExecutionResult> beginExecution(InstrumentationExecutionParameters parameters) {
        log.info("beginExecution");
        return super.beginExecution(parameters);
    }

    @Override
    public GraphQLSchema instrumentSchema(GraphQLSchema schema, InstrumentationExecutionParameters parameters) {
        log.info("instrumentSchema");
        return super.instrumentSchema(schema, parameters);
    }

    @Override
    public InstrumentationContext<Document> beginParse(InstrumentationExecutionParameters parameters) {
        log.info("beginParse");
        return super.beginParse(parameters);
    }

    @Override
    public DocumentAndVariables instrumentDocumentAndVariables(DocumentAndVariables documentAndVariables, InstrumentationExecutionParameters parameters) {
        log.info("instrumentDocumentAndVariables");
        return super.instrumentDocumentAndVariables(documentAndVariables, parameters);
    }

    @Override
    public InstrumentationContext<List<ValidationError>> beginValidation(InstrumentationValidationParameters parameters) {
        log.info("beginValidation");
        return super.beginValidation(parameters);
    }

    @Override
    public ExecutionContext instrumentExecutionContext(ExecutionContext executionContext, InstrumentationExecutionParameters parameters) {
        log.info("instrumentExecutionContext");
        return super.instrumentExecutionContext(executionContext, parameters);
    }

    @Override
    public InstrumentationContext<ExecutionResult> beginExecuteOperation(InstrumentationExecuteOperationParameters parameters) {
        log.info("beginExecuteOperation");
        return super.beginExecuteOperation(parameters);
    }

    @Override
    public ExecutionStrategyInstrumentationContext beginExecutionStrategy(InstrumentationExecutionStrategyParameters parameters) {
        log.info("beginExecutionStrategy");
        return super.beginExecutionStrategy(parameters);
    }

    @Override
    public InstrumentationContext<ExecutionResult> beginField(InstrumentationFieldParameters parameters) {
        log.info("beginField");
        return super.beginField(parameters);
    }

    @Override
    public InstrumentationContext<Object> beginFieldFetch(InstrumentationFieldFetchParameters parameters) {
        log.info("beginFieldFetch");
        return super.beginFieldFetch(parameters);
    }

    @Override
    public DataFetcher<?> instrumentDataFetcher(DataFetcher<?> dataFetcher, InstrumentationFieldFetchParameters parameters) {
        log.info("instrumentDataFetcher");
        return super.instrumentDataFetcher(dataFetcher, parameters);
    }

    @Override
    public InstrumentationContext<ExecutionResult> beginSubscribedFieldEvent(InstrumentationFieldParameters parameters) {
        log.info("beginSubscribedFieldEvent");
        return super.beginSubscribedFieldEvent(parameters);
    }

    @Override
    public InstrumentationContext<ExecutionResult> beginFieldComplete(InstrumentationFieldCompleteParameters parameters) {
        log.info("beginFieldComplete");
        return super.beginFieldComplete(parameters);
    }

    @Override
    public InstrumentationContext<ExecutionResult> beginFieldListComplete(InstrumentationFieldCompleteParameters parameters) {
        log.info("beginFieldListComplete");
        return super.beginFieldListComplete(parameters);
    }

    @Override
    public CompletableFuture<ExecutionResult> instrumentExecutionResult(ExecutionResult executionResult, InstrumentationExecutionParameters parameters) {
        log.info("instrumentExecutionResult");
        return super.instrumentExecutionResult(executionResult, parameters);
    }
}
