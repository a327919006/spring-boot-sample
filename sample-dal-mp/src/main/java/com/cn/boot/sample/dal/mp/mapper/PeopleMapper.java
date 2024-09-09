package com.cn.boot.sample.dal.mp.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.cn.boot.sample.dal.mp.model.po.People;

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
}
