package com.cn.boot.sample.api.model.dto;

import com.cn.boot.sample.api.model.Constants;

import java.io.Serializable;

/**
 * <p>Title: BaseRsp</p>
 * <p>Description: Http操作结果对象</p>
 */
public class BaseRsp implements Serializable {
    private int code = Constants.CODE_SUCCESS;
    private String msg = Constants.MSG_SUCCESS;
    private Object data;

    public BaseRsp() {
    }

    public BaseRsp code(final int code) {
        this.code = code;
        return this;
    }

    public BaseRsp msg(final String msg) {
        this.msg = msg;
        return this;
    }

    public BaseRsp data(final Object data) {
        this.data = data;
        return this;
    }

    public BaseRsp(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }


    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }


    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "BaseRsp{" +
                "code=" + code +
                ", msg='" + msg + '\'' +
                ", data=" + data +
                '}';
    }
}
