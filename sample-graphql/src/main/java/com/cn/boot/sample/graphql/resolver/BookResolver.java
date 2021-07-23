package com.cn.boot.sample.graphql.resolver;

import cn.hutool.core.date.DateUtil;
import com.cn.boot.sample.graphql.dao.AuthorDao;
import com.cn.boot.sample.graphql.entity.Author;
import com.cn.boot.sample.graphql.entity.Book;
import graphql.schema.DataFetchingEnvironment;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author Chen Nan
 */
@Slf4j
@Component
public class BookResolver {

    @Autowired
    private AuthorDao authorDao;

    public String updateTime(Book book) {
        return DateUtil.formatDateTime(book.getUpdateTime());
    }

    public Integer getPrice(DataFetchingEnvironment env) {
        String city = env.getArgumentOrDefault("city", "beijing");
        log.info("city={}", city);
        switch (city) {
            case "beijing":
                return 200;
            default:
                return 100;
        }
    }

    public Author getAuthor(Book book) {
        return authorDao.findAuthorById(book.getAuthorId());
    }
}