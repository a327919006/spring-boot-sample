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
 * @author Chen Nan
 */
@Slf4j
@Component
public class GraphqlExceptionHandler implements DataFetcherExceptionHandler {
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
