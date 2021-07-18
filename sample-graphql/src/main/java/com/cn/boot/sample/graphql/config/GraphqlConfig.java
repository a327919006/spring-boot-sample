package com.cn.boot.sample.graphql.config;

import com.cn.boot.sample.graphql.config.wiring.AuthorWiring;
import com.cn.boot.sample.graphql.config.wiring.BookWiring;
import com.cn.boot.sample.graphql.config.wiring.QueryWiring;
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
public class GraphqlConfig {

    private GraphQL graphQL;

    @Autowired
    private QueryWiring queryWiring;
    @Autowired
    private BookWiring bookWiring;
    @Autowired
    private AuthorWiring authorWiring;

    @Bean
    public GraphQL graphQL() {
        return graphQL;
    }

    @PostConstruct
    public void init() throws IOException {
        String queryPath = getResourcePath("graphql/schema.graphqls");
//        String authorPath = getResourcePath("graphql/schema.graphqls");
//        String bookPath = getResourcePath("graphql/schema.graphqls");

        SchemaParser schemaParser = new SchemaParser();
        SchemaGenerator schemaGenerator = new SchemaGenerator();
        TypeDefinitionRegistry typeRegistry = new TypeDefinitionRegistry();

        typeRegistry.merge(schemaParser.parse(queryPath));
//        typeRegistry.merge(schemaParser.parse(authorPath));
//        typeRegistry.merge(schemaParser.parse(bookPath));

        RuntimeWiring runtimeWiring = buildWiring();
        GraphQLSchema schema = schemaGenerator.makeExecutableSchema(typeRegistry, runtimeWiring);
        graphQL = GraphQL.newGraphQL(schema).build();
    }

    private String getResourcePath(String resource) throws IOException {
        URL url = Resources.getResource(resource);
        return Resources.toString(url, Charsets.UTF_8);
    }

    private RuntimeWiring buildWiring() {
        return RuntimeWiring.newRuntimeWiring()
                .type("Query", queryWiring)
                .type("Author", authorWiring)
                .type("Book", bookWiring)
                .build();
    }
}
