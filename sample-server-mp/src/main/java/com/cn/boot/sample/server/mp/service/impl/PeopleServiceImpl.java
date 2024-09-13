package com.cn.boot.sample.server.mp.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cn.boot.sample.dal.mp.mapper.PeopleMapper;
import com.cn.boot.sample.dal.mp.model.po.People;
import com.cn.boot.sample.server.mp.service.IPeopleService;
import org.springframework.stereotype.Service;

import java.util.List;

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

    @Override
    public List<People> list(People dto) {
        // 写法1：
        // QueryWrapper<People> queryWrapper = new QueryWrapper<>();
        // queryWrapper.select("id").eq("name", dto.getName());
        // return baseMapper.selectList(queryWrapper);
        // 写法2：
        // LambdaQueryWrapper<People> queryWrapper = new LambdaQueryWrapper<>();
        // queryWrapper.select(People::getId).eq(People::getName, dto.getName());
        // return baseMapper.selectList(queryWrapper);
        // 写法3：
        return new LambdaQueryChainWrapper<>(People.class)
                .select(People::getId)
                .eq(People::getName, dto.getName()).list();
    }
}
