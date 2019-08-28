package com.cn.boot.sample.api.model.po;

import lombok.Data;
import lombok.experimental.Accessors;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @author Chen Nan
 */
@Data
@Accessors(chain = true)
@Table(name = "boot_sample.sys_resource")
public class SysResource implements Serializable {
    /**
     * 资源唯一标识
     */
    @Id
    @Column(name = "resource_id")
    private String resourceId;

    /**
     * 资源名称
     */
    private String name;

    /**
     * 资源路径
     */
    private String url;

    /**
     * 资源类型（0：菜单；1：按钮）
     */
    private Byte type;

    /**
     * 资源图标
     */
    private String icon;

    /**
     * 资源显示顺序
     */
    private Integer priority;

    /**
     * 资源父编号
     */
    @Column(name = "parent_id")
    private String parentId;

    /**
     * 权限字符串
     */
    private String permission;

    /**
     * 资源状态（0：禁用；1：启用）
     */
    private Byte status;

    /**
     * 创建人
     */
    @Column(name = "create_user")
    private String createUser;

    /**
     * 资源创建时间
     */
    @Column(name = "create_time")
    private LocalDateTime createTime;

    /**
     * 更新人
     */
    @Column(name = "update_user")
    private String updateUser;

    /**
     * 资源更新时间
     */
    @Column(name = "update_time")
    private LocalDateTime updateTime;
}