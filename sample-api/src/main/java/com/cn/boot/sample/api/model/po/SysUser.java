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
@Table(name = "boot_sample.sys_user")
public class SysUser implements Serializable {
    /**
     * 系统用户编号
     */
    @Id
    @Column(name = "sys_user_id")
    private String sysUserId;

    /**
     * 用户名
     */
    @Column(name = "user_name")
    private String userName;

    /**
     * 用户状态(0:待审核 1:审核通过 2:审核不通过 -1:停用)
     */
    @Column(name = "user_status")
    private Byte userStatus;

    /**
     * 用户密码
     */
    @Column(name = "user_pwd")
    private String userPwd;

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