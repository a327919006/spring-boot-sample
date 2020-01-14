package com.cn.boot.sample.api.model.vo.student;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class StudentRsp implements Serializable {
    /**
     * 姓名
     */
    private String name;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;
}