package com.cn.boot.sample.dal.mapper;

import com.cn.boot.sample.api.model.po.UserRole;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

/**
 * @author Chen Nan
 */
public interface UserRoleMapper extends Mapper<UserRole> {

    /**
     * <p>根据用户Id获取用户角色记录列表</p>
     *
     * @param sysUserId 系统用户唯一标识
     */
    List<UserRole> selectByUserId(String sysUserId);

    /**
     * 删除多个用户主键对应的关联记录
     *
     * @param sysUserIds 系统用户唯一标识列表
     * @return
     */
    int deleteByUserIds(List<String> sysUserIds);

    /**
     * 删除多个角色主键对应的关联记录
     *
     * @param sysRoleIds 角色唯一标识列表
     * @return
     */
    int deleteByRoleIds(List<String> sysRoleIds);

}