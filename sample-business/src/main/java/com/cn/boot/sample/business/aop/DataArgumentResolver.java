package com.cn.boot.sample.business.aop;

import com.alibaba.fastjson.JSONObject;
import com.cn.boot.sample.api.model.dto.ReqDTO;
import com.cn.boot.sample.business.util.CheckUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import javax.servlet.http.HttpServletRequest;
import java.io.InputStream;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/**
 * 请求参数处理器，负责将加密参数解密
 *
 * @author Chen Nan
 */
@Slf4j
@Component
public class DataArgumentResolver implements HandlerMethodArgumentResolver {
    @Override
    public boolean supportsParameter(MethodParameter methodParameter) {
        Class<?> parameterType = methodParameter.getParameterType();
        return parameterType == ReqDTO.class;
    }

    @Override
    public Object resolveArgument(MethodParameter methodParameter, ModelAndViewContainer modelAndViewContainer,
                                  NativeWebRequest nativeWebRequest, WebDataBinderFactory webDataBinderFactory) throws Exception {
        // 获取请求参数，转换成ReqDTO
        HttpServletRequest request = nativeWebRequest.getNativeRequest(HttpServletRequest.class);
        InputStream inputStream = request.getInputStream();
        byte[] bytes = IOUtils.toByteArray(inputStream);
        String jsonStr = new String(bytes);
        ReqDTO reqDTO = (ReqDTO) JSONObject.parseObject(jsonStr, methodParameter.getParameterType());

        // 校验数据签名
        String data = (String) reqDTO.getData();
        boolean checkResult = CheckUtil.checkSign(reqDTO);
        reqDTO.setCheckSig(checkResult);
        if (checkResult) {
            // 请求参数解密
            String result = CheckUtil.aesDecrypt(data);

            // 获取ReqDTO泛型类型
            Type parameterizedType = methodParameter.getParameter().getParameterizedType();
            Type actualType = ((ParameterizedType) parameterizedType).getActualTypeArguments()[0];

            // 将data转成泛型类型
            Object paramObject = JSONObject.parseObject(result, actualType);
            reqDTO.setData(paramObject);
        }
        return reqDTO;
    }

}
