package com.cn.boot.sample.graphql.resolver;

import com.cn.boot.sample.graphql.dao.AuthorDao;
import com.cn.boot.sample.graphql.dao.BookDao;
import com.cn.boot.sample.graphql.entity.Author;
import com.cn.boot.sample.graphql.entity.Book;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author Chen Nan
 */
@Component
public class Query {

    @Autowired
    private AuthorDao authorDao;

    @Autowired
    private BookDao bookDao;

    public Author findAuthorById(String id) {
        return authorDao.findAuthorById(Long.parseLong(id));
    }

    public List<Author> findAllAuthors() {
        return authorDao.findAll();
    }

    public Long countAuthors() {
        return authorDao.count();
    }

    public List<Book> findAllBooks() {
        return bookDao.findAll();
    }

    public Long countBooks() {
        return bookDao.count();
    }
}
