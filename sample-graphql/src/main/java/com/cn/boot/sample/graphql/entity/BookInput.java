package com.cn.boot.sample.graphql.entity;

import lombok.Data;

/**
 * @author Chen Nan
 */
@Data
public class BookInput {
    private String title;

    private String isbn;

    private int pageCount;

    private long authorId;
}