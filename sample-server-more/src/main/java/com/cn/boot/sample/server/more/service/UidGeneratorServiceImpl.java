package com.cn.boot.sample.server.more.service;

import com.baidu.fsg.uid.UidGenerator;
import com.cn.boot.sample.api.service.UidGeneratorService;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.Service;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * uid生成器服务实现类
 *
 * @author Chen Nan
 */
@Service(timeout = 30000)
@Slf4j
public class UidGeneratorServiceImpl implements UidGeneratorService {
    @Autowired
    private UidGenerator uidGenerator;

    @Override
    public String generate() {
        return String.valueOf(uidGenerator.getUID());
    }
}
