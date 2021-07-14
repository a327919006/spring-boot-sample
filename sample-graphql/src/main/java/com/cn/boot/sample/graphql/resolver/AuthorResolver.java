package com.cn.boot.sample.graphql.resolver;

import com.cn.boot.sample.graphql.dao.BookDao;
import com.cn.boot.sample.graphql.entity.Author;
import com.cn.boot.sample.graphql.entity.Book;
import graphql.schema.idl.RuntimeWiring;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.graphql.boot.RuntimeWiringBuilderCustomizer;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.List;

/**
 * @author Chen Nan
 */
@Component
public class AuthorResolver implements RuntimeWiringBuilderCustomizer {

    private static final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    @Autowired
    private BookDao bookDao;

    public String getCreatedTime(Author author) {
        return sdf.format(author.getCreatedTime());
    }

    public List<Book> getBooks(Author author) {
        return bookDao.findByAuthorId(author.getId());
    }

    @Override
    public void customize(RuntimeWiring.Builder builder) {
        builder.type("Query", wiring ->
                wiring.dataFetcher("books", env -> getBooks(env.getArgument("id"))));
    }
}
