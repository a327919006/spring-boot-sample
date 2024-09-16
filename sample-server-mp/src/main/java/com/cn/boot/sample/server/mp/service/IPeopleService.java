package com.cn.boot.sample.server.mp.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.cn.boot.sample.dal.mp.model.dto.PeopleDTO;
import com.cn.boot.sample.dal.mp.model.po.People;
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
    /**
     * 分页查询
     */
    IPage<PeopleVO> pageVO(PeopleDTO dto);

    /**
     * 查询列表
     */
    List<PeopleVO> list(People dto);

    /**
     * 修改账户余额
     */
    boolean updateAccount(PeopleDTO dto);
}
