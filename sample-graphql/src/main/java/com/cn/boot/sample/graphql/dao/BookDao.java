package com.cn.boot.sample.graphql.dao;

import com.cn.boot.sample.graphql.entity.Book;
import com.google.common.collect.Lists;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;


/**
 * @author Chen Nan
 */
@Component
public class BookDao {

    public List<Book> findByAuthorId(Long id) {
        Book book = new Book();
        book.setId(1L);
        book.setAuthorId(id);
        book.setTitle("title" + id);
        book.setIsbn("isbn" + id);
        book.setCostPrice(10);
        book.setPageCount(1);
        book.setCreateTime(new Date());
        book.setUpdateTime(new Date());

        return Lists.newArrayList(book);
    }

    public Book findBookById(Long id) {
        Book book = new Book();
        book.setId(id);
        book.setAuthorId(2L);
        book.setTitle("title2");
        book.setIsbn("isbn2");
        book.setCostPrice(10);
        book.setPageCount(2);
        book.setCreateTime(new Date());
        book.setUpdateTime(new Date());
        return book;
    }

    public List<Book> findAll() {
        Book book = new Book();
        book.setId(1L);
        book.setAuthorId(1L);
        book.setTitle("title1");
        book.setIsbn("isbn1");
        book.setCostPrice(10);
        book.setPageCount(1);
        book.setCreateTime(new Date());
        book.setUpdateTime(new Date());

        return Lists.newArrayList(book);
    }

    public Long count() {
        return 1L;
    }
}
