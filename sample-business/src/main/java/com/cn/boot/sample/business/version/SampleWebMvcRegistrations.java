package com.cn.boot.sample.business.version;

import org.springframework.boot.autoconfigure.web.servlet.WebMvcRegistrations;
import org.springframework.web.servlet.mvc.method.annotation.ExceptionHandlerExceptionResolver;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

/**
 * @author Chen Nan
 */
public class SampleWebMvcRegistrations implements WebMvcRegistrations {

    public RequestMappingHandlerMapping getRequestMappingHandlerMapping() {
        return new SampleRequestMappingHandlerMapping();
    }

    public RequestMappingHandlerAdapter getRequestMappingHandlerAdapter() {
        return null;
    }

    public ExceptionHandlerExceptionResolver getExceptionHandlerExceptionResolver() {
        return null;
    }
}