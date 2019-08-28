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
@Table(name = "boot_sample.role_resource")
public class RoleResource implements Serializable {
    /**
     * 角色资源唯一标识
     */
    @Id
    @Column(name = "role_resource_id")
    private String roleResourceId;

    /**
     * 角色唯一标识
     */
    @Column(name = "role_id")
    private String roleId;

    /**
     * 资源唯一标识
     */
    @Column(name = "resource_id")
    private String resourceId;

    /**
     * 记录创建时间
     */
    @Column(name = "create_time")
    private LocalDateTime createTime;

    /**
     * 记录更新时间
     */
    @Column(name = "update_time")
    private LocalDateTime updateTime;
}