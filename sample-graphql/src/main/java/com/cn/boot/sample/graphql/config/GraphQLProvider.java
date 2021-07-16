package com.cn.boot.sample.graphql.config;

import cn.hutool.core.date.DateUtil;
import com.cn.boot.sample.graphql.entity.Author;
import com.cn.boot.sample.graphql.resolver.AuthorResolver;
import com.cn.boot.sample.graphql.resolver.BookResolver;
import com.cn.boot.sample.graphql.resolver.Query;
import com.google.common.io.Resources;
import graphql.GraphQL;
import graphql.com.google.common.base.Charsets;
import graphql.schema.GraphQLSchema;
import graphql.schema.idl.RuntimeWiring;
import graphql.schema.idl.SchemaGenerator;
import graphql.schema.idl.SchemaParser;
import graphql.schema.idl.TypeDefinitionRegistry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.net.URL;

/**
 * @author Chen Nan
 */
@Component
public class GraphQLProvider {

    private GraphQL graphQL;

    @Autowired
    private Query query;
    @Autowired
    private BookResolver bookResolver;
    @Autowired
    private AuthorResolver authorResolver;

    @Bean
    public GraphQL graphQL() {
        return graphQL;
    }

    @PostConstruct
    public void init() throws IOException {
        URL url = Resources.getResource("graphql/schema.graphqls");
        String sdl = Resources.toString(url, Charsets.UTF_8);
        GraphQLSchema graphQLSchema = buildSchema(sdl);
        graphQL = GraphQL.newGraphQL(graphQLSchema).build();
    }

    private GraphQLSchema buildSchema(String sdl) {
        TypeDefinitionRegistry typeRegistry = new SchemaParser().parse(sdl);
        RuntimeWiring runtimeWiring = buildWiring();
        SchemaGenerator schemaGenerator = new SchemaGenerator();
        return schemaGenerator.makeExecutableSchema(typeRegistry, runtimeWiring);
    }

    private RuntimeWiring buildWiring() {
//        return RuntimeWiring.newRuntimeWiring()
//                .type(newTypeWiring("Query").dataFetcher("bookById", query.getAuthorDataFetcher()))
//                .type(newTypeWiring("Book").dataFetcher("author", query.getAuthorDataFetcher()))
//                .build();

        return RuntimeWiring.newRuntimeWiring()
                .type("Query", wiring -> wiring
                        .dataFetcher("findAuthorById", env -> query.findAuthorById(env.getArgument("id")))
                        .dataFetcher("findAllAuthors", env -> query.findAllAuthors())
                        .dataFetcher("countAuthors", env -> query.countAuthors())
                        .dataFetcher("findAllBooks", env -> query.findAllBooks())
                        .dataFetcher("countBooks", env -> query.countBooks())
                )
                .type("Author", wiring -> wiring
                        .dataFetcher("books", env -> authorResolver.getBooks(env.getSource()))
                        .dataFetcher("updateTime", env -> DateUtil.formatDateTime(((Author) env.getSource()).getUpdateTime()))
                )
                .type("Book", wiring -> wiring
                        .dataFetcher("author", env -> bookResolver.getAuthor(env.getSource()))
                )
                .build();
    }
}
