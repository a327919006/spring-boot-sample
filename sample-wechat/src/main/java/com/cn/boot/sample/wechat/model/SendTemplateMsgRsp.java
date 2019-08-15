package com.cn.boot.sample.wechat.model;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Chen Nan
 */
@NoArgsConstructor
@Data
public class SendTemplateMsgRsp {

    /**
     * errcode : 0
     * errmsg : ok
     * msgid : 200228332
     */

    private int errcode;
    private String errmsg;
    private long msgid;
}
