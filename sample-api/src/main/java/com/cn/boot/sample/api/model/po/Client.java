package com.cn.boot.sample.api.model.po;

import java.io.Serializable;
import java.time.LocalDateTime;

public class Client implements Serializable {
    private Long id;

    private String platId;

    private String name;

    private Byte status;

    private Integer threshold;

    private Integer repeatSecond;

    private Byte ossType;

    private String ossConfigId;

    private Byte thirdType;

    private String thirdAppId;

    private String thirdSecretId;

    private String thirdSecretKey;

    private String thirdUserId;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPlatId() {
        return platId;
    }

    public void setPlatId(String platId) {
        this.platId = platId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Byte getStatus() {
        return status;
    }

    public void setStatus(Byte status) {
        this.status = status;
    }

    public Integer getThreshold() {
        return threshold;
    }

    public void setThreshold(Integer threshold) {
        this.threshold = threshold;
    }

    public Integer getRepeatSecond() {
        return repeatSecond;
    }

    public void setRepeatSecond(Integer repeatSecond) {
        this.repeatSecond = repeatSecond;
    }

    public Byte getOssType() {
        return ossType;
    }

    public void setOssType(Byte ossType) {
        this.ossType = ossType;
    }

    public String getOssConfigId() {
        return ossConfigId;
    }

    public void setOssConfigId(String ossConfigId) {
        this.ossConfigId = ossConfigId;
    }

    public Byte getThirdType() {
        return thirdType;
    }

    public void setThirdType(Byte thirdType) {
        this.thirdType = thirdType;
    }

    public String getThirdAppId() {
        return thirdAppId;
    }

    public void setThirdAppId(String thirdAppId) {
        this.thirdAppId = thirdAppId;
    }

    public String getThirdSecretId() {
        return thirdSecretId;
    }

    public void setThirdSecretId(String thirdSecretId) {
        this.thirdSecretId = thirdSecretId;
    }

    public String getThirdSecretKey() {
        return thirdSecretKey;
    }

    public void setThirdSecretKey(String thirdSecretKey) {
        this.thirdSecretKey = thirdSecretKey;
    }

    public String getThirdUserId() {
        return thirdUserId;
    }

    public void setThirdUserId(String thirdUserId) {
        this.thirdUserId = thirdUserId;
    }

    public LocalDateTime getCreateTime() {
        return createTime;
    }

    public void setCreateTime(LocalDateTime createTime) {
        this.createTime = createTime;
    }

    public LocalDateTime getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(LocalDateTime updateTime) {
        this.updateTime = updateTime;
    }
}