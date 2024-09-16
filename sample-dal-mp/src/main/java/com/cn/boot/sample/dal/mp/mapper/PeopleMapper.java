package com.cn.boot.sample.dal.mp.mapper;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.cn.boot.sample.dal.mp.model.dto.PeopleDTO;
import com.cn.boot.sample.dal.mp.model.po.People;
import com.cn.boot.sample.dal.mp.model.vo.PeopleVO;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;

/**
 * <p>
 * 人员信息 Mapper 接口
 * </p>
 *
 * @author Chen Nan
 * @since 2024-09-08
 */
public interface PeopleMapper extends BaseMapper<People> {
    /**
     * 分页查询
     *
     * @param page 分页条件
     * @param dto  查询条件
     * @return 分页查询结果
     */
    IPage<PeopleVO> pageVO(IPage<PeopleVO> page, @Param("dto") PeopleDTO dto);

    /**
     * 修改账户余额
     *
     * @param account 账户变动金额
     * @param wrapper 查询条件
     * @return 更新结果
     */
    int updateAccount(@Param("account") BigDecimal account,
                      @Param(Constants.WRAPPER) LambdaQueryWrapper<People> wrapper);
}
