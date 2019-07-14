package com.cn.boot.sample.dal.mapper;

import com.cn.boot.sample.api.model.po.RoleResource;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

/**
 * @author Chen Nan
 */
public interface RoleResourceMapper extends Mapper<RoleResource> {
    /**
     * <p>根据角色Id获取角色资源记录列表</p>
     *
     * @param sysRoleId 角色唯一标识
     * @return
     */
    List<RoleResource> selectByRoleId(String sysRoleId);

    /**
     * 删除多个主键对应的关联记录
     *
     * @param sysRoleIds 角色唯一标识列表
     * @return
     */
    int deleteByRoleIds(List<String> sysRoleIds);

    /**
     * 删除多个主键对应的关联记录
     *
     * @param sysResourceIds 资源唯一标识列表
     * @return
     */
    int deleteByResourceIds(List<String> sysResourceIds);
}