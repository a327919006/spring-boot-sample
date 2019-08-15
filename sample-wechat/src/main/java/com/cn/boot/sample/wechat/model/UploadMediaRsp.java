package com.cn.boot.sample.wechat.model;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Chen Nan
 */
@NoArgsConstructor
@Data
public class UploadMediaRsp {
    /**
     * type : TYPE
     * media_id : MEDIA_ID
     * created_at : 123456789
     */
    private int errcode;
    private String type;
    private String media_id;
    private int created_at;
}
