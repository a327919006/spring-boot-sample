package com.cn.boot.sample.server.service;

import cn.hutool.core.util.IdUtil;
import cn.hutool.crypto.SecureUtil;
import com.cn.boot.sample.api.exceptions.BusinessException;
import com.cn.boot.sample.api.model.dto.DataGrid;
import com.cn.boot.sample.api.model.dto.system.SysResourceDTO;
import com.cn.boot.sample.api.model.dto.system.SysUserDTO;
import com.cn.boot.sample.api.model.po.SysUser;
import com.cn.boot.sample.api.model.po.UserRole;
import com.cn.boot.sample.api.service.SysUserService;
import com.cn.boot.sample.dal.mapper.SysResourceMapper;
import com.cn.boot.sample.dal.mapper.SysUserMapper;
import com.cn.boot.sample.dal.mapper.UserRoleMapper;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.config.annotation.Service;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

/**
 * 系统用户服务实现类
 *
 * @author Chen Nan
 * @date 2019/3/11.
 */
@Service(timeout = 300000)
public class SysUserServiceImpl extends BaseServiceImpl<SysUserMapper, SysUser, String>
        implements SysUserService {

    @Autowired
    private UserRoleMapper sysUserRoleMapper;
    @Autowired
    private SysResourceMapper sysResourceMapper;

    @Override
    public void insertUser(SysUser req, SysUser sessionUser) {
        List<SysUser> users = mapper.selectByUserName(req.getUserName());
        if (null != users && users.size() > 0) {
            throw new BusinessException("该用户已存在");
        } else {
            req.setCreateUser(sessionUser.getUserName());

            // 新增用户
            SysUser newUser = new SysUser();
            BeanUtils.copyProperties(req, newUser);
            newUser.setSysUserId(IdUtil.simpleUUID());
            newUser.setUserPwd(SecureUtil.md5(newUser.getUserPwd()));
            newUser.setCreateTime(LocalDateTime.now());
            newUser.setUpdateTime(LocalDateTime.now());
            mapper.insertSelective(newUser);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public SysUser selectByUserNameAndPassWord(String username, String password) {
        return mapper.selectByUserNameAndPassWord(username, password);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<SysUser> selectByUserName(String username) {
        return mapper.selectByUserName(username);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int deleteByPrimaryKeys(List<String> userIds) {
        sysUserRoleMapper.deleteByUserIds(userIds);
        return mapper.deleteByPrimaryKeys(userIds);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public DataGrid selectByConditionPage(SysUserDTO model) {
        PageHelper.startPage(model.getPage(), model.getRows());
        List<SysUserDTO> list = mapper.selectByConditionPage(model);
        DataGrid dataGrid = new DataGrid();
        dataGrid.setRows(list);
        dataGrid.setTotal((int) ((Page) list).getTotal());
        return dataGrid;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int countByCondition(SysUserDTO model) {
        return mapper.countByCondition(model);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<UserRole> selectUserRoleByUserId(String userId) {
        return sysUserRoleMapper.selectByUserId(userId);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int allotUserRole(String userId, List<String> roleIds) {
        sysUserRoleMapper.deleteByUserIds(Arrays.asList(userId));
        int ret = 0;
        for (String roleId : roleIds) {
            if (StringUtils.isNotBlank(roleId)) {
                UserRole userRole = new UserRole();
                userRole.setId(IdUtil.simpleUUID());
                userRole.setSysUserId(userId);
                userRole.setRoleId(roleId);
                userRole.setCreateTime(LocalDateTime.now());
                sysUserRoleMapper.insert(userRole);
                ret++;
            }
        }
        return ret;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<SysResourceDTO> selectMenuByUserId(String userId) {
        return sysResourceMapper.selectMenuByUserId(userId);
    }

    @Override
    public List<SysResourceDTO> selectMenuByImsUserId(String imsUserId) {
        return null;
    }
}
