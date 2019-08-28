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
@Table(name = "boot_sample.user_role")
public class UserRole implements Serializable {
    /**
     * 系统编号
     */
    @Id
    private String id;

    /**
     * 系统用户编号
     */
    @Column(name = "sys_user_id")
    private String sysUserId;

    /**
     * 角色编号
     */
    @Column(name = "role_id")
    private String roleId;

    /**
     * 创建时间
     */
    @Column(name = "create_time")
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    @Column(name = "update_time")
    private LocalDateTime updateTime;
}