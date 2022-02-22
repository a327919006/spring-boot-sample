package com.cn.boot.sample.business.aop;

import com.alibaba.fastjson.JSONObject;
import com.cn.boot.sample.api.enums.ErrorCode;
import com.cn.boot.sample.api.exceptions.BusinessException;
import com.cn.boot.sample.api.model.dto.ReqDTO;
import com.cn.boot.sample.business.util.CheckUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.InputStream;

/**
 * @author Chen Nan
 */
@Slf4j
public class CheckInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        SampleRequestWrapper sampleRequestWrapper = new SampleRequestWrapper(request);
        InputStream inputStream = sampleRequestWrapper.getInputStream();
        byte[] bytes = IOUtils.toByteArray(inputStream);
        String jsonStr = new String(bytes);
        ReqDTO reqDTO = JSONObject.parseObject(jsonStr, ReqDTO.class);

        // 校验数据签名
        boolean checkResult = CheckUtil.checkSign(reqDTO);
        reqDTO.setCheckSig(checkResult);
        if (!checkResult) {
            throw new BusinessException(ErrorCode.ERROR_SIG);
        }
        return true;
    }
}
