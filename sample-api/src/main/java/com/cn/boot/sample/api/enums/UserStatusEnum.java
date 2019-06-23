package com.cn.boot.sample.api.enums;

/**
 * @author Chen Nan
 */
public enum UserStatusEnum {
    // 用户状态
    STOP(0, "停用"),
    NORMAL(1, "正常");

    private Integer value;

    private String desc;

    UserStatusEnum(Integer value, String desc) {
        this.value = value;
        this.desc = desc;
    }

    public Integer getValue() {
        return value;
    }

    public void setValue(Integer value) {
        this.value = value;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }
}
