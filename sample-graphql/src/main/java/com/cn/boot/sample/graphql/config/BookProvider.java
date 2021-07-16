package com.cn.boot.sample.graphql.config;

import com.cn.boot.sample.graphql.dao.BookDao;
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
public class BookProvider {

    @Autowired
    private BookDao bookDao;


    @PostConstruct
    public void init() throws IOException {

    }

    public List<Book> findAllBooks() {
        return bookDao.findAll();
    }

    public Long countBooks() {
        return bookDao.count();
    }

    @Bean
    private RuntimeWiring bookRuntimeWiring() {
        return RuntimeWiring.newRuntimeWiring()
                .type("Query", wiring -> wiring
                        .dataFetcher("findAllBooks", env -> findAllBooks())
                        .dataFetcher("countBooks", env -> countBooks()))
                .build();
    }
}
