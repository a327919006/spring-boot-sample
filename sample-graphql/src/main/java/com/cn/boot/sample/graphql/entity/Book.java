package com.cn.boot.sample.graphql.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author Chen Nan
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class Book extends BaseEntity {
    private String title;

    private String isbn;

    private int pageCount;

    private long authorId;
}
