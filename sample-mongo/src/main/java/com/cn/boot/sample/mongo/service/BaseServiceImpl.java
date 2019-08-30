package com.cn.boot.sample.mongo.service;

import com.cn.boot.sample.mongo.dao.BaseMongoDao;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * <p>Title: BaseServiceImpl</p>
 * <p>Description: 基本服务实现类</p>
 * <p>
 * M dao
 * T 对象类型
 * PK 主键类型
 *
 * @author Chen Nan
 */
@SuppressWarnings("unchecked")
@Slf4j
public abstract class BaseServiceImpl<D extends BaseMongoDao, T, PK> {
    /**
     * 持久层对象
     */
    @Autowired
    protected D dao;

    /**
     * 根据主键删除
     *
     * @param id 根据主键删除
     */
    public int deleteByPrimaryKey(PK id) {
        return dao.deleteByPrimaryKey(id);
    }

    /**
     * 插入数据
     */
    public T insert(T record) {
        return (T) dao.insert(record);
    }

    /**
     * 根据主键查询
     */
    public T selectByPrimaryKey(PK id) {
        return (T) dao.selectByPrimaryKey(id);
    }

    /**
     * 根据主键更新数据
     */
    public int updateByPrimaryKey(T record) {
        return dao.updateByPrimaryKey(record);
    }

    /**
     * 获取数量
     */
    public int count(T record) {
        return dao.count(record);
    }

    /**
     * 获取单条数据
     */
    public T get(T record) {
        return (T) dao.get(record);
    }

    /**
     * 获取列表
     */
    public List<T> list(T record) {
        return dao.list(record);
    }
}
