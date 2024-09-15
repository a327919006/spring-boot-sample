package com.cn.boot.sample.dal.mp.mapper;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.cn.boot.sample.dal.mp.model.po.People;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;

/**
 * <p>
 * 人员信息 Mapper 接口
 * </p>
 *
 * @author Chen Nan
 * @since 2024-09-08
 */
public interface PeopleMapper extends BaseMapper<People> {
    People getByName(String name);

    int updateAccount(@Param("account") BigDecimal account, @Param(Constants.WRAPPER) LambdaQueryWrapper<People> wrapper);
}
