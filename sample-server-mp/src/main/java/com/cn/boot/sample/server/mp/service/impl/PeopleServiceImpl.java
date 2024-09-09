package com.cn.boot.sample.server.mp.service.impl;

import com.cn.boot.sample.dal.mp.model.po.People;
import com.cn.boot.sample.dal.mp.mapper.PeopleMapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cn.boot.sample.server.mp.service.IPeopleService;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 人员信息 服务实现类
 * </p>
 *
 * @author Chen Nan
 * @since 2024-09-08
 */
@Service
public class PeopleServiceImpl extends ServiceImpl<PeopleMapper, People> implements IPeopleService {

    @Override
    public People getByName(String name) {
        return baseMapper.getByName(name);
    }
}
