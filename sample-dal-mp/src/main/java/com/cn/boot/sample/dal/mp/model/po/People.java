package com.cn.boot.sample.dal.mp.model.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 * 人员信息
 * </p>
 * TableName使用说明：当表明与类名不一致时，如表明叫t_people
 * TableId使用说明：1、当主键不叫id时可定义主键字段名，2、ID类型：AUTO自增，INPUT自定义，自动设置ASSIGN_ID雪花ID
 * TableField使用说明：1、当字段名与属性名不一致 2、关键字转义如`order`需加转义字符 3、属性不是数据库字段
 *
 * @author Chen Nan
 * @since 2024-09-08
 */
@Getter
@Setter
@TableName("t_people")
@ApiModel(value = "People对象", description = "人员信息")
public class People extends BaseEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    @ApiModelProperty("姓名")
    @TableField("name")
    private String name;

    @TableField(exist = false)
    private Integer page;
}
