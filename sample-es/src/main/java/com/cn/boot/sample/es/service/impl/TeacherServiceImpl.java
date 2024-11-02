package com.cn.boot.sample.es.service.impl;

import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateUtil;
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
import org.springframework.util.CollectionUtils;

import java.util.Date;
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
        req.setCreateTime(new Date());
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

    @Override
    public String sql() {
        String sql = "select age,count(*) from teacher group by age";
        return dao.executeSQL(sql);
    }

    /**
     * 构造查询条件
     */
    private LambdaEsQueryChainWrapper<Teacher> buildCondition(TeacherReq req) {
        return EsWrappers.lambdaChainQuery(dao)
                .eq(StringUtils.isNoneEmpty(req.getName()), Teacher::getName, req.getName())
                .eq(req.getAge() != null && req.getAge() > 0, Teacher::getAge, req.getAge())
                .ge(req.getStartTime() != null, Teacher::getCreateTime,
                        DateUtil.format(req.getStartTime(), DatePattern.NORM_DATETIME_MS_FORMAT))
                .le(req.getEndTime() != null, Teacher::getCreateTime,
                        DateUtil.format(req.getEndTime(), DatePattern.NORM_DATETIME_MS_FORMAT))
                .in(!CollectionUtils.isEmpty(req.getNameList()), Teacher::getName, req.getNameList())
                .match(StringUtils.isNotEmpty(req.getRemark()), Teacher::getRemark, req.getRemark())
                .orderByDesc(StringUtils.isNotEmpty(req.getOrderBy()), req.getOrderBy())
                // 支持声明只返回指定字段或不返回指定字段
                // .select(Teacher::getName)
                // .notSelect(Teacher::getRemark)
                ;
    }

}
