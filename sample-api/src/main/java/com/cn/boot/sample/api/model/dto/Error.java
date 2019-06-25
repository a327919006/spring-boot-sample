package com.cn.boot.sample.api.model.dto;

import com.cn.boot.sample.api.model.Constants;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * <p>Title: RspBase</p>
 * <p>Description: Http操作结果对象</p>
 *
 * @author Chen Nan
 * @date 2019/3/11.
 */
@Data
@NoArgsConstructor
@Accessors(chain = true)
public class Error implements Serializable {
    private Integer code = Constants.CODE_FAILURE;
    private String msg = Constants.MSG_SUCCESS;

    public Error(String msg) {
        this.msg = msg;
    }

    public Error(Integer code, String msg) {
        if (code == null) {
            code = Constants.CODE_FAILURE;
        }
        this.code = code;
        this.msg = msg;
    }
}
