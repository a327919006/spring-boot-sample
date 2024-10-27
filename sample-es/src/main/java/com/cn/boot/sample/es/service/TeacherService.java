package com.cn.boot.sample.es.service;

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
     * 保存消息
     *
     * @param teacher 消息内容
     */
    void save(Teacher teacher);

    /**
     * 获取所有消息
     *
     * @return 消息列表
     */
    List<Teacher> findAll();

    /**
     * 根据name搜索
     *
     * @param name 姓名
     * @return 学生列表
     */
    EsPageInfo<Teacher> findByName(String name, int page, int size);
}
