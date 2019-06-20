package com.cn.boot.sample.server.service;

import com.cn.boot.sample.api.service.IUidGeneratorService;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @author Chen Nan
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@Slf4j
public class UidGeneratorTest {

    @Autowired
    private IUidGeneratorService uidGeneratorService;

    @Test
    public void generate() {
        long uid = uidGeneratorService.generate();
        log.info("uid = " + uid);
    }
}
