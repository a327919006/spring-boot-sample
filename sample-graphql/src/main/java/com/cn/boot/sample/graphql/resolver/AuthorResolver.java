package com.cn.boot.sample.graphql.resolver;

import com.cn.boot.sample.graphql.dao.BookDao;
import com.cn.boot.sample.graphql.entity.Author;
import com.cn.boot.sample.graphql.entity.Book;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.List;

/**
 * @author Chen Nan
 */
@Component
public class AuthorResolver {

    private static final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    @Autowired
    private BookDao bookDao;

    public String getCreatedTime(Author author) {
        return sdf.format(author.getCreateTime());
    }

    public List<Book> getBooks(Author author) {
        return bookDao.findByAuthorId(author.getId());
    }
}
