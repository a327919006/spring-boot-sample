package com.cn.boot.sample.api.service;

import com.cn.boot.sample.api.model.po.SensitiveWord;

import java.util.List;

/**
 * 敏感词服务
 *
 * @author Chen Nan
 */
public interface SensitiveWordService extends BaseService<SensitiveWord, Long> {
    /**
     * 添加敏感词
     *
     * @param appId         数据源key
     * @param sensitiveWord 敏感词信息
     * @return 操作数量
     */
    int insertSensitiveWord(String appId, SensitiveWord sensitiveWord);

    /**
     * 添加敏感词，带事务
     *
     * @param appId         数据源key
     * @param sensitiveWord 敏感词信息
     * @return 操作数量
     */
    int insertWithTransaction(String appId, SensitiveWord sensitiveWord);

    /**
     * 获取敏感词列表
     *
     * @param appId         数据源key
     * @param sensitiveWord 查询条件
     * @return 敏感词列表
     */
    List<SensitiveWord> list(String appId, SensitiveWord sensitiveWord);
}
