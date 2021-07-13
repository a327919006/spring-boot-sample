package com.cn.boot.sample.graphql.resolver;

import com.cn.boot.sample.graphql.dao.AuthorDao;
import com.cn.boot.sample.graphql.entity.Author;
import com.cn.boot.sample.graphql.entity.Book;
import graphql.schema.idl.RuntimeWiring;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.graphql.boot.RuntimeWiringBuilderCustomizer;
import org.springframework.stereotype.Component;

/**
 * @author Chen Nan
 */
@Component
public class BookResolver implements RuntimeWiringBuilderCustomizer {

    @Autowired
    private AuthorDao authorDao;

    public Author getAuthor(Book book) {
        return authorDao.findAuthorById(book.getAuthorId());
    }

    @Override
    public void customize(RuntimeWiring.Builder builder) {
        builder.type("Query", wiring ->
                wiring.dataFetcher("author", env -> authorDao.findAuthorById(env.getArgument("authorId"))));
    }
}