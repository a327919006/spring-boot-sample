package com.cn.boot.sample.business.aop;

import org.apache.commons.io.IOUtils;

import javax.servlet.ReadListener;
import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import java.io.*;

/**
 * @author Chen Nan
 */
public class SampleRequestWrapper extends HttpServletRequestWrapper {
    /**
     * 参数字节数组
     */
    private byte[] requestBody;
    /**
     * Http请求对象
     */
    private HttpServletRequest request;

    public SampleRequestWrapper(HttpServletRequest request) {
        super(request);
        this.request = request;
    }

    @Override
    public ServletInputStream getInputStream() throws IOException {
        if (null == this.requestBody) {
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            IOUtils.copy(request.getInputStream(), outputStream);
            this.requestBody = outputStream.toByteArray();
        }

        final ByteArrayInputStream inputStream = new ByteArrayInputStream(requestBody);
        return new ServletInputStream() {

            @Override
            public boolean isFinished() {
                return false;
            }

            @Override
            public boolean isReady() {
                return false;
            }

            @Override
            public void setReadListener(ReadListener listener) {

            }

            @Override
            public int read() {
                return inputStream.read();
            }
        };
    }

    public byte[] getRequestBody() {
        return requestBody;
    }

    @Override
    public BufferedReader getReader() throws IOException {
        return new BufferedReader(new InputStreamReader(this.getInputStream()));
    }
}
