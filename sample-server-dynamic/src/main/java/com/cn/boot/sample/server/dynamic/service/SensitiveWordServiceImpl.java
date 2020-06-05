package com.cn.boot.sample.server.dynamic.service;

import com.cn.boot.sample.api.model.po.SensitiveWord;
import com.cn.boot.sample.api.service.SensitiveWordService;
import com.cn.boot.sample.dal.mapper.SensitiveWordMapper;
import com.cn.boot.sample.server.dynamic.config.dds.DS;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.Service;

import javax.annotation.Resource;

/**
 * @author Chen Nan
 */
@Service(timeout = 300000)
@Slf4j
public class SensitiveWordServiceImpl extends BaseServiceImpl<SensitiveWordMapper, SensitiveWord, Long>
        implements SensitiveWordService {

    @Resource
    private SensitiveWordMapper sensitiveWordMapper;

    @Override
    @DS
    public int insertSensitiveWord(String appId, SensitiveWord sensitiveWord) {
        return sensitiveWordMapper.insertSelective(sensitiveWord);
    }
}
