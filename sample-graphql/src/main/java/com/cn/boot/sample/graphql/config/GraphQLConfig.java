package com.cn.boot.sample.graphql.config;

import com.google.common.io.Resources;
import graphql.GraphQL;
import graphql.com.google.common.base.Charsets;
import graphql.schema.GraphQLSchema;
import graphql.schema.idl.RuntimeWiring;
import graphql.schema.idl.SchemaGenerator;
import graphql.schema.idl.SchemaParser;
import graphql.schema.idl.TypeDefinitionRegistry;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;
import java.net.URL;

/**
 * @author Chen Nan
 */
@Configuration
public class GraphQLConfig {

    @Bean
    public GraphQL initGraphQL(RuntimeWiring bookRuntimeWiring) throws IOException {
        URL bookUrl = Resources.getResource("graphql/book.graphqls");
        String bookSdl = Resources.toString(bookUrl, Charsets.UTF_8);
        URL authorUrl = Resources.getResource("graphql/author.graphqls");
        String authorSdl = Resources.toString(bookUrl, Charsets.UTF_8);

        SchemaParser schemaParser = new SchemaParser();

        TypeDefinitionRegistry bookTypeRegistry = schemaParser.parse(bookSdl);
        TypeDefinitionRegistry authorTypeRegistry = schemaParser.parse(authorSdl);

        SchemaGenerator schemaGenerator = new SchemaGenerator();
        GraphQLSchema graphQLSchema = schemaGenerator.makeExecutableSchema(bookTypeRegistry, bookRuntimeWiring);

        return GraphQL.newGraphQL(graphQLSchema).build();
    }
}
