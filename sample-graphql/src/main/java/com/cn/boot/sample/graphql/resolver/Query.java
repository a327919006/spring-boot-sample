package com.cn.boot.sample.graphql.resolver;

import com.cn.boot.sample.graphql.dao.AuthorDao;
import com.cn.boot.sample.graphql.dao.BookDao;
import com.cn.boot.sample.graphql.entity.Author;
import com.cn.boot.sample.graphql.entity.Book;
import com.coxautodev.graphql.tools.GraphQLQueryResolver;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author Chen Nan
 */
@Component
@AllArgsConstructor
public class Query implements GraphQLQueryResolver {

    private AuthorDao authorDao;

    private BookDao bookDao;

    public Author findAuthorById(Long id) {
        return authorDao.findAuthorById(id);
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
