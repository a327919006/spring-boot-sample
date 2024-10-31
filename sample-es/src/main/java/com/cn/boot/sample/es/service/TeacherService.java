package com.cn.boot.sample.es.service;

import com.cn.boot.sample.es.model.dto.TeacherReq;
import com.cn.boot.sample.es.model.po.Teacher;
import org.dromara.easyes.core.biz.EsPageInfo;

import java.util.List;

/**
 * @author Chen Nan
 */
public interface TeacherService {
    /**
     * 创建索引
     *
     * @return 操作结果
     */
    boolean createIndex();

    /**
     * 删除索引
     *
     * @return 操作结果
     */
    boolean deleteIndex();

    /**
     * 新增
     */
    Integer insert(Teacher req);

    /**
     * 更新
     */
    Integer update(Teacher req);

    /**
     * 根据ID删除
     */
    Integer deleteById(String id);

    /**
     * 根据ID获取
     */
    Teacher getById(String id);

    /**
     * 获取所有
     */
    List<Teacher> list(TeacherReq req);

    /**
     * 分页查询
     */
    EsPageInfo<Teacher> page(TeacherReq req);

    /**
     * 获取数量
     */
    long count(TeacherReq req);


}
