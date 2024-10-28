package com.cn.boot.sample.es.service.impl;

import com.cn.boot.sample.es.dao.TeacherDao;
import com.cn.boot.sample.es.model.po.Teacher;
import com.cn.boot.sample.es.service.TeacherService;
import lombok.extern.slf4j.Slf4j;
import org.dromara.easyes.core.biz.EsPageInfo;
import org.dromara.easyes.core.kernel.EsWrappers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author Chen Nan
 */
@Slf4j
@Service("teacherService")
public class TeacherServiceImpl implements TeacherService {

    @Autowired
    private TeacherDao dao;

    @Override
    public boolean createIndex() {
        return dao.createIndex();
    }

    @Override
    public boolean deleteIndex() {
        return dao.deleteIndex();
    }

    @Override
    public Integer save(Teacher docBean) {
        return dao.insert(docBean);
    }

    @Override
    public Integer update(Teacher req) {
        return dao.updateById(req);
    }

    @Override
    public Teacher getById(String id) {
        return dao.selectById(id);
    }

    @Override
    public Integer delete(String id) {
        return dao.deleteById(id);
    }

    @Override
    public List<Teacher> findAll() {
        return EsWrappers.lambdaChainQuery(dao).list();
    }

    @Override
    public EsPageInfo<Teacher> findByName(String name, int page, int size) {
        return EsWrappers.lambdaChainQuery(dao).eq(Teacher::getName, name).page(page, size);
    }

    @Override
    public long countByName(String name) {
        return EsWrappers.lambdaChainQuery(dao).eq(Teacher::getName, name).count();
    }

}
