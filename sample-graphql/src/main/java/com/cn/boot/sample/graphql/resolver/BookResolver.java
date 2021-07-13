package com.cn.boot.sample.graphql.resolver;

import com.cn.boot.sample.graphql.dao.AuthorDao;
import com.cn.boot.sample.graphql.entity.Author;
import com.cn.boot.sample.graphql.entity.Book;
import com.coxautodev.graphql.tools.GraphQLResolver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author Chen Nan
 */
@Component
public class BookResolver implements GraphQLResolver<Book> {

    @Autowired
    private AuthorDao authorDao;

    public Author getAuthor(Book book) {
        return authorDao.findAuthorById(book.getAuthorId());
    }
}