package com.cn.boot.sample.dal.mp.model.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.handlers.JacksonTypeHandler;
import com.cn.boot.sample.dal.mp.enums.PeopleStatus;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * <p>
 * 人员信息
 * </p>
 * TableName使用说明：当表明与类名不一致时，如表明叫t_people; autoResultMap当属性中有json类型时需为true，否则查询结果不会映射到字段
 * TableId使用说明：1、当主键不叫id时可定义主键字段名，2、ID类型：AUTO自增，INPUT自定义，自动设置ASSIGN_ID雪花ID
 * TableField使用说明：1、当字段名与属性名不一致 2、关键字转义如`order`需加转义字符 3、属性不是数据库字段exist=false
 *
 * @author Chen Nan
 * @since 2024-09-08
 */
@Getter
@Setter
@Accessors(chain = true)
@TableName(value = "t_people", autoResultMap = true)
@ApiModel(value = "People对象", description = "人员信息")
public class People extends BaseEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    @ApiModelProperty("姓名")
    @TableField("name")
    private String name;

    @ApiModelProperty("账户余额")
    @TableField("account")
    private BigDecimal account;

    /**
     * 枚举类型，使用mp中自带的枚举类型处理器
     */
    @ApiModelProperty("状态 0待审核 1审核中 2审核成功 3审核失败")
    @TableField("status")
    private PeopleStatus status;

    /**
     * JSON类型，使用mp中自带的JSON类型处理器
     */
    @ApiModelProperty("地址")
    @TableField(value = "address", typeHandler = JacksonTypeHandler.class)
    private Address address;
}
