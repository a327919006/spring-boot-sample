package com.cn.boot.sample.graphql.config.wiring;

import com.cn.boot.sample.graphql.resolver.BookResolver;
import graphql.schema.idl.TypeRuntimeWiring.Builder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.function.UnaryOperator;

/**
 * @author Chen Nan
 */
@Component
public class BookWiring implements UnaryOperator<Builder> {
    @Autowired
    private BookResolver bookResolver;

    @Override
    public Builder apply(Builder builder) {
        return builder
                .dataFetcher("author", env -> bookResolver.getAuthor(env.getSource()));
    }
}
