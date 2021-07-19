package com.cn.boot.sample.graphql.resolver;

import cn.hutool.core.date.DateUtil;
import com.cn.boot.sample.graphql.dao.AuthorDao;
import com.cn.boot.sample.graphql.entity.Author;
import com.cn.boot.sample.graphql.entity.Book;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author Chen Nan
 */
@Component
public class BookResolver {

    @Autowired
    private AuthorDao authorDao;

    public String getCreatedTime(Book book) {
        return DateUtil.formatDateTime(book.getUpdateTime());
    }

    public Integer getPrice() {
        return 100;
    }

    public Author getAuthor(Book book) {
        return authorDao.findAuthorById(book.getAuthorId());
    }
}