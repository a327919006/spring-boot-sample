package com.cn.boot.sample.dal.mp.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * TableName使用说明：当表明与类名不一致时，如表明叫t_people
 * TableId使用说明：1、当主键不叫id时，2、ID类型：AUTO自增，INPUT自定义，自动设置ASSIGN_ID雪花ID
 * TableField使用说明：1、当字段名与属性名不一致 2、关键字转义如`order`需加转义字符 3、属性不是数据库字段
 * @author Chen Nan
 */
@TableName("people")// 表明
@Data
public class People implements Serializable {
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    @TableField(value = "name")
    private String name;

    private Date createTime;

    private Date updateTime;

    @TableField(exist = false)
    private Integer page;
}
