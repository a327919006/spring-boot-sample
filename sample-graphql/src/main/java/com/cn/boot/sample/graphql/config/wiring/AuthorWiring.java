package com.cn.boot.sample.graphql.config.wiring;

import cn.hutool.core.date.DateUtil;
import com.cn.boot.sample.graphql.entity.Author;
import com.cn.boot.sample.graphql.resolver.AuthorResolver;
import graphql.schema.idl.TypeRuntimeWiring.Builder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.function.UnaryOperator;

/**
 * @author Chen Nan
 */
@Component
public class AuthorWiring implements UnaryOperator<Builder> {
    @Autowired
    private AuthorResolver authorResolver;

    @Override
    public Builder apply(Builder builder) {
        return builder
                .dataFetcher("books", env -> authorResolver.getBooks(env.getSource()))
                .dataFetcher("updateTime", env -> DateUtil.formatDateTime(((Author) env.getSource()).getUpdateTime()));
    }
}
