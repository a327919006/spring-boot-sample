package com.cn.boot.sample.business.aop;

import com.cn.boot.sample.api.model.Constants;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Chen Nan
 */
@ControllerAdvice
public class CommonRspAdvice implements ResponseBodyAdvice<Object> {

    @Override
    public boolean supports(MethodParameter returnType, Class<? extends HttpMessageConverter<?>> converterType) {
        boolean jsonConverter = MappingJackson2HttpMessageConverter.class.isAssignableFrom(converterType);
        boolean notExe = !returnType.hasMethodAnnotation(ExceptionHandler.class);
        if (jsonConverter && notExe) {
            Class<?> declaringClass = returnType.getDeclaringClass();
            String testController = "com.cn.boot.sample.business.controller.ClientController";
            return StringUtils.startsWithIgnoreCase(declaringClass.getName(), testController);
        } else {
            return false;
        }
    }

    @Override
    public Object beforeBodyWrite(Object body, MethodParameter returnType, MediaType selectedContentType, Class<? extends HttpMessageConverter<?>> selectedConverterType, ServerHttpRequest request, ServerHttpResponse response) {
        Map<String, Object> rsp = new HashMap<>(3);
        rsp.put("msg", Constants.MSG_SUCCESS);
        rsp.put("code", Constants.CODE_SUCCESS);
        rsp.put("data", body);
        return rsp;
    }
}
