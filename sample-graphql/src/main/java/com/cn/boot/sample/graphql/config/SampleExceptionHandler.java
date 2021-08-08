package com.cn.boot.sample.graphql.config;

import com.cn.boot.sample.graphql.exception.BusinessException;
import graphql.GraphQLError;
import graphql.GraphqlErrorBuilder;
import graphql.execution.DataFetcherExceptionHandler;
import graphql.execution.DataFetcherExceptionHandlerParameters;
import graphql.execution.DataFetcherExceptionHandlerResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * 图查询异常处理类，可在发生异常时获取异常信息，可用于异常上报或其他异常处理流程
 *
 * @author Chen Nan
 */
@Slf4j
@Component
public class SampleExceptionHandler implements DataFetcherExceptionHandler {

    @Override
    public DataFetcherExceptionHandlerResult onException(DataFetcherExceptionHandlerParameters parameters) {
        Throwable exception = parameters.getException();
        GraphQLError error = null;
        if (exception instanceof BusinessException) {
            error = (BusinessException) exception;
        } else {
//            GraphQLError error = GraphqlErrorBuilder.newError(parameters.getDataFetchingEnvironment()).build();
            error = GraphqlErrorBuilder.newError()
                    .message(exception.getMessage())
                    .location(parameters.getSourceLocation())
                    .path(parameters.getPath())
                    .build();
        }

        log.error("获取数据异常:", exception);
        return DataFetcherExceptionHandlerResult.newResult(error).build();
    }
}
