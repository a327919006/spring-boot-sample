package com.cn.boot.sample.guava.io;

import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * @author Chen Nan
 */
@Data
public class CommitInfo {
    private String type;
    private String num;
    private List<String> authorList;
    private Date first;
    private Date last;

}
