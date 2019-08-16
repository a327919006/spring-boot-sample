package com.cn.boot.sample.api.model.dto.chat;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Chen Nan
 */
@NoArgsConstructor
@Data
public class ChatRsp {
    /**
     * result : 0
     * content : 网友，你不知道你叫啥？
     */

    private int result;
    private String content;
}
