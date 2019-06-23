package com.cn.boot.sample.security.controller;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @author Chen Nan
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@Slf4j
public class SecurityTest {

    @Test
    public void createPassword() {
        String password = "123456";
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String encodedPassword = passwordEncoder.encode(password);
        log.info("【encodedPassword】= " + encodedPassword);
    }
}
