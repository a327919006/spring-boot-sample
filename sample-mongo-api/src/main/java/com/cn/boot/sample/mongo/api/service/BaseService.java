package com.cn.boot.sample.mongo.api.service;

import java.util.List;

/**
 * <p>Title: IBaseService</p>
 * <p>Description: 基本服务接口</p>
 *
 * @param <T>  记录数据类型
 * @param <PK> 记录主键类型
 * @author Chen Nan
 */
public interface BaseService<T, PK> {

    /**
     * 根据主键删除
     *
     * @param id 根据主键删除
     * @return 操作数量
     */
    int deleteByPrimaryKey(PK id);

    /**
     * 插入数据
     *
     * @param record 对象数据
     * @return 操作数量
     */
    T insert(T record);

    /**
     * 根据主键查询
     *
     * @param id 主键
     * @return 对象
     */
    T selectByPrimaryKey(PK id);

    /**
     * 根据主键更新数据
     *
     * @param record 对象数据
     * @return 操作数量
     */
    int updateByPrimaryKey(T record);

    /**
     * 获取数量
     *
     * @param record 对象参数
     * @return 数量
     */
    int count(T record);

    /**
     * 获取单条数据
     *
     * @param record 对象参数
     * @return 对象
     */
    T get(T record);

    /**
     * 获取列表
     *
     * @param record 对象数据
     * @return 对象列表
     */
    List<T> list(T record);
}