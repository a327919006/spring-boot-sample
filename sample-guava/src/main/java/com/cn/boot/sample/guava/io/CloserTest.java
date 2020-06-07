package com.cn.boot.sample.guava.io;

import com.google.common.io.Closer;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.util.ResourceUtils;

import java.io.*;

import static org.junit.jupiter.api.TestInstance.Lifecycle.PER_CLASS;

/**
 * @author Chen Nan
 */
@Slf4j
@TestInstance(PER_CLASS)
public class CloserTest {

    private File file;

    @BeforeAll
    public void init() throws FileNotFoundException {
        file = ResourceUtils.getFile("classpath:test.txt");
    }

    /**
     * 原先习惯，下面这种关闭流写法（try catch finally）
     * 下面模拟处理业务时发生了异常，并且在关闭流时也发生了异常
     * 运行后会发现，跑出的异常信息只包含了关闭流的异常信息，原本排查业务需要的业务异常信息丢失了
     */
    @Test
    public void tryCatchFinally() {
        BufferedReader inputStream = null;
        try {
            inputStream = new BufferedReader(new FileReader(file));
            String line = inputStream.readLine();
            // 模拟处理业务发生异常
            throw new RuntimeException("1");
        } catch (Exception e) {
            throw e;
        } finally {
            // 此处关闭流，模拟关闭流时发生异常
//            if(inputStream != null){
//                inputStream.close();
//            }
            throw new RuntimeException("2");
        }
    }

    /**
     * 为了避免异常信息丢失，可以使用下面这种写法addSuppressed
     */
    @Test
    public void tryCatchFinally2() {
        Throwable t = null;
        try {
            // 模拟处理业务发生异常
            throw new RuntimeException("1");
        } catch (Exception e) {
            t = e;
            throw e;
        } finally {
            // 模拟关闭流时发生异常
            RuntimeException runtimeException = new RuntimeException("2");
            runtimeException.addSuppressed(t);
            throw runtimeException;
        }
    }

    @Test
    public void closer() throws IOException {
        Closer closer = Closer.create();
        try {
            BufferedReader inputStream = new BufferedReader(new FileReader(file));
            closer.register(inputStream);
            // 模拟处理业务发生异常
            throw new RuntimeException("1");
        } catch (Exception e) {
            throw closer.rethrow(e);
        } finally {
            closer.close();
        }
    }
}
