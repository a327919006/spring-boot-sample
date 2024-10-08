package com.cn.boot.sample.dal.mp.model.po;

import com.baomidou.mybatisplus.annotation.OrderBy;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableLogic;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * @author Chen Nan
 */
@Data
public class BaseEntity {
    @TableField("deleted")
    @TableLogic
    private Byte deleted;

    @ApiModelProperty("创建时间")
    @TableField("create_time")
    private LocalDateTime createTime;

    @ApiModelProperty("更新时间")
    @TableField("update_time")
    @OrderBy
    private LocalDateTime updateTime;
}
