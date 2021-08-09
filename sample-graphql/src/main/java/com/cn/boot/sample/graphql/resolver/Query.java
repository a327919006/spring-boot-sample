package com.cn.boot.sample.graphql.resolver;

import com.cn.boot.sample.api.model.po.User;
import com.cn.boot.sample.graphql.dao.AuthorDao;
import com.cn.boot.sample.graphql.dao.BookDao;
import com.cn.boot.sample.graphql.entity.Author;
import com.cn.boot.sample.graphql.entity.Book;
import com.cn.boot.sample.graphql.exception.BusinessException;
import graphql.schema.DataFetchingEnvironment;
import graphql.schema.DataFetchingFieldSelectionSet;
import graphql.schema.SelectedField;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author Chen Nan
 */
@Slf4j
@Component
public class Query {

    @Autowired
    private AuthorDao authorDao;

    @Autowired
    private BookDao bookDao;

    public String test() {
        return "sample-graphql";
    }

    public String testError() {
        throw new BusinessException("testError");
    }

    public String testError2() {
        throw new RuntimeException("testError2");
    }

    public Author findAuthorById(String id) {
        return authorDao.findAuthorById(Long.parseLong(id));
    }

    public List<Author> findAllAuthors() {
        return authorDao.findAll();
    }

    public Long countAuthors() {
        return authorDao.count();
    }

    public List<Book> findAllBooks(DataFetchingEnvironment env) {
        // 获取上下文信息
        Object context = env.getContext();
        if (context instanceof User) {
            User user = (User) context;
            log.info("user:{}", user);
        } else {
            log.info(context.toString());
        }

        // 查看本次调用请求的字段列表
        DataFetchingFieldSelectionSet selectionSet = env.getSelectionSet();
        if (selectionSet.contains("title")) {
            log.info("contains title");
            List<SelectedField> nodeFields = selectionSet.getFields("author/*");
            nodeFields.forEach(selectedField -> {
                        log.info(selectedField.getName());
                        log.info(selectedField.getType().toString());

                        DataFetchingFieldSelectionSet innerSelectionSet = selectedField.getSelectionSet();
                        // this forms a tree of selection and you can get very fancy with it
                    }
            );
        }
        return bookDao.findAll();
    }

    public Long countBooks() {
        return bookDao.count();
    }
}
