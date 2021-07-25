package com.cn.boot.sample.graphql.config.wiring;

import com.cn.boot.sample.graphql.resolver.Query;
import graphql.schema.idl.TypeRuntimeWiring;
import graphql.schema.idl.TypeRuntimeWiring.Builder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.function.UnaryOperator;

/**
 * @author Chen Nan
 */
@Component
public class QueryWiring implements UnaryOperator<Builder> {
    @Autowired
    private Query query;

    @Override
    public TypeRuntimeWiring.Builder apply(TypeRuntimeWiring.Builder builder) {
        return builder
                .dataFetcher("test", env -> query.test())
                .dataFetcher("testError", env -> query.testError())
                .dataFetcher("findAuthorById", env -> query.findAuthorById(env.getArgument("id")))
                .dataFetcher("findAllAuthors", env -> query.findAllAuthors())
                .dataFetcher("countAuthors", env -> query.countAuthors())
                .dataFetcher("findAllBooks", env -> query.findAllBooks())
                .dataFetcher("countBooks", env -> query.countBooks());
    }
}
