package com.cn.boot.sample.api.model.po;

import java.io.Serializable;
import java.time.LocalDateTime;
import javax.persistence.*;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author Chen Nan
 */
@Data
@Accessors(chain = true)
@Table(name = "boot_sample.sys_role")
public class SysRole implements Serializable {
    /**
     * 角色编号
     */
    @Id
    @Column(name = "role_id")
    private String roleId;

    /**
     * 角色名称
     */
    @Column(name = "role_name")
    private String roleName;

    /**
     * 状态(1:正常 -1:停用)
     */
    private Byte status;

    /**
     * 创建人
     */
    @Column(name = "create_user")
    private String createUser;

    /**
     * 创建时间
     */
    @Column(name = "create_time")
    private LocalDateTime createTime;

    /**
     * 更新人
     */
    @Column(name = "update_user")
    private String updateUser;

    /**
     * 更新时间
     */
    @Column(name = "update_time")
    private LocalDateTime updateTime;
}