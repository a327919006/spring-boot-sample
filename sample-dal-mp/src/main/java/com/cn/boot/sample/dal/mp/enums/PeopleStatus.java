package com.cn.boot.sample.dal.mp.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;

/**
 * @author Chen Nan
 */
@Getter
public enum PeopleStatus {
    // 人员审核状态
    NO(0, "待审核"),
    AUDITING(1, "审核中"),
    SUCCESS(2, "审核成功"),
    FAIL(3, "审核失败");

    @EnumValue
    private final int value;
    @JsonValue
    private final String desc;

    PeopleStatus(int value, String desc) {
        this.value = value;
        this.desc = desc;
    }
}
