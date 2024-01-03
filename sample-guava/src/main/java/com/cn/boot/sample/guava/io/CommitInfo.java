package com.cn.boot.sample.guava.io;

import cn.hutool.core.date.DateUtil;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;

import java.util.Date;
import java.util.List;
import java.util.Set;

/**
 * @author Chen Nan
 */
@Data
public class CommitInfo {
    private String type;
    private String num;
    private Set<String> authorList;
    private Date first;
    private Date last;

    @Override
    public String toString() {
        String typeStr = "";
        if (StringUtils.equals(type, CommitParseTest.typeFeat)) {
            typeStr = "需求";
        } else {
            typeStr = "BUG";
        }
        return typeStr + "\t"
                + num + "\t"
                + DateUtil.formatDate(first) + "\t"
                + DateUtil.formatDate(last) + "\t"
                + authorList;
    }
}
