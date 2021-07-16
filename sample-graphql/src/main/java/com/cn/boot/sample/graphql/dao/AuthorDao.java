package com.cn.boot.sample.graphql.dao;

import com.cn.boot.sample.graphql.entity.Author;
import com.google.common.collect.Lists;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;

/**
 * @author Chen Nan
 */
@Component
public class AuthorDao {

    public Author findAuthorById(Long id) {
        Author author = new Author();
        author.setId(id);
        author.setFirstName("1111");
        author.setLastName("222");
        author.setCreateTime(new Date());
        author.setUpdateTime(new Date());

        return author;
    }

    public List<Author> findAll() {
        Author author = new Author();
        author.setId(1L);
        author.setFirstName("1111");
        author.setLastName("222");
        author.setCreateTime(new Date());
        author.setUpdateTime(new Date());

        return Lists.newArrayList(author);
    }

    public Long count() {
        return 1L;
    }
}
