package com.cn.boot.sample.es.service.impl;

import com.cn.boot.sample.es.dao.TeacherDao;
import com.cn.boot.sample.es.model.dto.TeacherReq;
import com.cn.boot.sample.es.model.po.Teacher;
import com.cn.boot.sample.es.service.TeacherService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.dromara.easyes.core.biz.EsPageInfo;
import org.dromara.easyes.core.conditions.select.LambdaEsQueryChainWrapper;
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
        String indexName = Teacher.class.getSimpleName().toLowerCase();
        return dao.deleteIndex(indexName);
    }

    @Override
    public Integer insert(Teacher req) {
        req.setId(null);
        return dao.insert(req);
    }

    @Override
    public Integer update(Teacher req) {
        return dao.updateById(req);
    }

    @Override
    public Integer deleteById(String id) {
        return dao.deleteById(id);
    }

    @Override
    public Teacher getById(String id) {
        return dao.selectById(id);
    }

    @Override
    public List<Teacher> list(TeacherReq req) {
        return buildCondition(req).list();
    }

    @Override
    public EsPageInfo<Teacher> page(TeacherReq req) {
        return buildCondition(req).page(req.getPage(), req.getSize());
    }

    @Override
    public long count(TeacherReq req) {
        return buildCondition(req).count();
    }

    /**
     * 构造查询条件
     */
    private LambdaEsQueryChainWrapper<Teacher> buildCondition(TeacherReq req) {
        return EsWrappers.lambdaChainQuery(dao)
                .eq(StringUtils.isNoneEmpty(req.getName()), Teacher::getName, req.getName())
                .eq(req.getAge() != null, Teacher::getAge, req.getAge());
    }

}
