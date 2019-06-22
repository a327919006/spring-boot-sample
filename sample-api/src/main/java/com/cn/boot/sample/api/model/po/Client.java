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
@Table(name = "boot_sample.client")
public class Client implements Serializable {
    /**
     * 商户ID
     */
    @Id
    private String id;

    /**
     * 平台ID
     */
    @Column(name = "plat_id")
    private String platId;

    /**
     * 商户名称
     */
    private String name;

    /**
     * 商户状态（0-无效，1-有效）
     */
    private Integer status;

    /**
     * 人脸相似度阈值
     */
    private Integer threshold;

    /**
     * 去重间隔（秒）
     */
    @Column(name = "repeat_second")
    private Integer repeatSecond;

    /**
     * 第三方存储类型 0腾讯 1瑞为 2华为
     */
    @Column(name = "oss_type")
    private Integer ossType;

    /**
     * 第三方存储配置ID
     */
    @Column(name = "oss_config_id")
    private String ossConfigId;

    /**
     * 第三方算法类型 0腾讯 1瑞为 2华为
     */
    @Column(name = "third_type")
    private Integer thirdType;

    /**
     * 第三方应用APPID
     */
    @Column(name = "third_app_id")
    private String thirdAppId;

    /**
     * 第三方应用SecretId
     */
    @Column(name = "third_secret_id")
    private String thirdSecretId;

    /**
     * 第三方应用密钥
     */
    @Column(name = "third_secret_key")
    private String thirdSecretKey;

    /**
     * 第三方应用userId
     */
    @Column(name = "third_user_id")
    private String thirdUserId;

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