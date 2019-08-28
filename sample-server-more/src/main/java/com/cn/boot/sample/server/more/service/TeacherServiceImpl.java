package com.cn.boot.sample.server.more.service;

import com.cn.boot.sample.api.model.po.Teacher;
import com.cn.boot.sample.api.service.TeacherService;
import com.cn.boot.sample.server.more.mapper.TeacherMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.Service;

/**
 * @author Chen Nan
 */
@Service
@Slf4j
public class TeacherServiceImpl extends BaseServiceImpl<TeacherMapper, Teacher, String>
        implements TeacherService {
}
