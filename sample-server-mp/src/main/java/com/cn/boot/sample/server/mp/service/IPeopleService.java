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
     *
     * @param dto 查询条件
     * @return 分页查询结果
     */
    IPage<PeopleVO> pageVO(PeopleDTO dto);

    /**
     * 查询列表
     *
     * @param dto 查询条件
     * @return 查询结果
     */
    List<PeopleVO> list(People dto);

    /**
     * 修改账户余额
     *
     * @param dto 账户及金额信息
     * @return 变更
     */
    boolean updateAccount(PeopleDTO dto);
}
