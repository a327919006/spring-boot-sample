package com.cn.boot.sample.graphql.config;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import graphql.ExecutionInput;
import graphql.execution.preparsed.PreparsedDocumentEntry;
import graphql.execution.preparsed.PreparsedDocumentProvider;
import org.springframework.stereotype.Component;

import java.util.function.Function;

/**
 * 作用：相同的语句，只要解析、校验一次，提高性能
 *
 * @author Chen Nan
 */
@Component
public class GraphqlPreparsedDocumentProvider implements PreparsedDocumentProvider {
    private final Cache<String, PreparsedDocumentEntry> CACHE = Caffeine.newBuilder().maximumSize(10_000).build();

    @Override
    public PreparsedDocumentEntry getDocument(ExecutionInput executionInput, Function<ExecutionInput,
            PreparsedDocumentEntry> function) {
        Function<String, PreparsedDocumentEntry> mapCompute = key -> function.apply(executionInput);
        return CACHE.get(executionInput.getQuery(), mapCompute);
    }
}
