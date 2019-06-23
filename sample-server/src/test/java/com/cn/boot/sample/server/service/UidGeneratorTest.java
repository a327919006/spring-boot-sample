package com.cn.boot.sample.server.service;

import com.cn.boot.sample.api.service.UidGeneratorService;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Chen Nan
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@Slf4j
public class UidGeneratorTest {

    @Autowired
    private UidGeneratorService uidGeneratorService;

    @Test
    public void generate() {
        // 1741115137720895
        Map<String, String> map = new HashMap<>();
        int existCount = 0;
        for (int i = 0; i < 1000; i++) {
            String uid = uidGeneratorService.generate();
            if (map.get(uid) != null) {
                existCount++;
            }
            map.put(uid, uid);
            log.info("uid = " + uid);
        }
        log.info("existCount = " + existCount);
    }
}
