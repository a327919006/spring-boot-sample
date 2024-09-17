package com.cn.boot.sample.server.mp.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cn.boot.sample.dal.mp.mapper.PeopleMapper;
import com.cn.boot.sample.dal.mp.model.dto.PeopleDTO;
import com.cn.boot.sample.dal.mp.model.po.People;
import com.cn.boot.sample.dal.mp.model.vo.PeopleVO;
import com.cn.boot.sample.server.mp.service.IPeopleService;
import org.apache.commons.lang3.StringUtils;
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
    public IPage<PeopleVO> pageVO(PeopleDTO dto) {
        if (dto.getCurrent() == null) {
            dto.setCurrent(1L);
        }
        if (dto.getSize() == null) {
            dto.setSize(10L);
        }
        Page<PeopleVO> page = Page.of(dto.getCurrent(), dto.getSize(), dto.getCurrent() == 1);
        page.addOrder(OrderItem.desc("update_time"));
        return baseMapper.pageVO(page, dto);
    }

    @Override
    public List<PeopleVO> list(People dto) {
        List<People> list;
        // 写法1：QueryWrapper
        // QueryWrapper<People> queryWrapper = new QueryWrapper<>();
        // queryWrapper.select("id").eq("name", dto.getName());
        // list = baseMapper.selectList(queryWrapper);

        // 写法2：静态工具 Wrappers + LambdaQueryWrapper
        // list = baseMapper.selectList(Wrappers.lambdaQuery(People.class)
        //         .select(People::getId)
        //         .eq(People::getName, dto.getName()));

        // 写法3：LambdaQueryChainWrapper
        // list = new LambdaQueryChainWrapper<>(People.class)
        //         .select(People::getId, People::getAccount)
        //         .eq(StringUtils.isNoneEmpty(dto.getName()), People::getName, dto.getName())
        //         .list();

        // 写法4：lambdaQuery
        list = lambdaQuery().select(People::getId, People::getAccount)
                .eq(StringUtils.isNoneEmpty(dto.getName()), People::getName, dto.getName())
                .list();

        return BeanUtil.copyToList(list, PeopleVO.class);
    }

    @Override
    public boolean updateAccount(PeopleDTO dto) {
        // 写法1：update+LambdaQueryWrappere
        // People entity = new People();
        // entity.setAccount(dto.getAccount());
        // return update(entity, new LambdaQueryWrapper<People>().eq(People::getName, dto.getName()));

        // 写法2：LambdaUpdateWrapper
        // return update(new LambdaUpdateWrapper<People>().setSql("account = account + " + dto.getAccount())
        //         .eq(People::getName, dto.getName()));

        // 写法3：LambdaUpdateWrapper
        // return lambdaUpdate().setSql("account = account + "+ dto.getAccount())
        //         .eq(People::getName, dto.getName())
        //         .update();

        // 写法4：自定义SQL
        return baseMapper.updateAccount(dto.getAccount(),
                new LambdaQueryWrapper<People>().eq(People::getDeleted, 0)
                        .eq(People::getName, dto.getName())) > 0;
    }
}
