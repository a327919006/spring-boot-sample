package com.cn.boot.sample.server.mp.service;

import com.cn.boot.sample.dal.mp.model.dto.PeopleDTO;
import com.cn.boot.sample.dal.mp.model.po.People;
import com.baomidou.mybatisplus.extension.service.IService;
import com.cn.boot.sample.dal.mp.model.vo.PeopleVO;

import java.util.List;

/**
 * <p>
 * 人员信息 服务类
 * </p>
 *
 * @author Chen Nan
 * @since 2024-09-08
 */
public interface IPeopleService extends IService<People> {
    People getByName(String name);

    List<PeopleVO> list(People dto);

    boolean updateAccount(PeopleDTO dto);

}
