package com.cn.boot.sample.dal.mp.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author Chen Nan
 */
public interface MyBaseMapper<T> extends BaseMapper<T> {
    int insertBatchSomeColumn(List<T> entityList);

    int alwaysUpdateSomeColumnById(@Param(Constants.ENTITY) T entity);
}
