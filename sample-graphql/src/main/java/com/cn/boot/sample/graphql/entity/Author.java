package com.cn.boot.sample.graphql.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author Chen Nan
 */

@Data
@EqualsAndHashCode(callSuper = false)
public class Author extends BaseEntity {

    private String firstName;

    private String lastName;
}