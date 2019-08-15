package com.cn.boot.sample.wechat.model;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Chen Nan
 */
@NoArgsConstructor
@Data
public class CreateQrCodeTicketRsp {
    /**
     * ticket : gQH47joAAAAAAAAAASxodHRwOi8vd2VpeGluLnFxLmNvbS9xL2taZ2Z3TVRtNzJXV1Brb3ZhYmJJAAIEZ23sUwMEmm 3sUw==
     * expire_seconds : 60
     * url : http://weixin.qq.com/q/kZgfwMTm72WWPkovabbI
     */

    private String ticket;
    private int expire_seconds;
    private String url;
}
