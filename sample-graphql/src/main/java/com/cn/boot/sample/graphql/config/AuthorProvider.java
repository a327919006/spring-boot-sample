package com.cn.boot.sample.graphql.config;

import com.cn.boot.sample.graphql.dao.AuthorDao;
import com.cn.boot.sample.graphql.dao.BookDao;
import com.cn.boot.sample.graphql.entity.Author;
import com.cn.boot.sample.graphql.entity.Book;
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
import java.util.List;

/**
 * @author Chen Nan
 */
@Component
public class AuthorProvider {

    private GraphQL graphQL;

    @Autowired
    private AuthorDao authorDao;

    @Bean
    public GraphQL graphQL() {
        return graphQL;
    }

    @PostConstruct
    public void init() throws IOException {
        URL url = Resources.getResource("graphql/author.graphqls");
        String sdl = Resources.toString(url, Charsets.UTF_8);
        GraphQLSchema graphQLSchema = buildSchema(sdl);
        this.graphQL = GraphQL.newGraphQL(graphQLSchema).build();
    }

    private GraphQLSchema buildSchema(String sdl) {
        TypeDefinitionRegistry typeRegistry = new SchemaParser().parse(sdl);
        RuntimeWiring runtimeWiring = buildWiring();
        SchemaGenerator schemaGenerator = new SchemaGenerator();
        return schemaGenerator.makeExecutableSchema(typeRegistry, runtimeWiring);
    }

    public Author findAuthorById(String id) {
        return authorDao.findAuthorById(Long.parseLong(id));
    }

    public List<Author> findAllAuthors() {
        return authorDao.findAll();
    }

    public Long countAuthors() {
        return authorDao.count();
    }

    private RuntimeWiring buildWiring() {
        return RuntimeWiring.newRuntimeWiring()
                .type("Query", wiring -> wiring
                        .dataFetcher("findAuthorById", env -> findAuthorById(env.getArgument("id")))
                        .dataFetcher("findAllAuthors", env -> findAllAuthors())
                        .dataFetcher("countAuthors", env -> countAuthors())
                )
                .build();
    }
}
