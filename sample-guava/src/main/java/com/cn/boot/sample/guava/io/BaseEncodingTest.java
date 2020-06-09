package com.cn.boot.sample.guava.io;

import com.google.common.io.BaseEncoding;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

/**
 * @author Chen Nan
 */
@Slf4j
public class BaseEncodingTest {

    @Test
    public void encode(){
        BaseEncoding baseEncoding = BaseEncoding.base64();
        log.info(baseEncoding.encode("hello".getBytes()));
    }

    @Test
    public void decode(){
        BaseEncoding baseEncoding = BaseEncoding.base64();
        log.info(new String(baseEncoding.decode("aGVsbG8=")));
    }
}
