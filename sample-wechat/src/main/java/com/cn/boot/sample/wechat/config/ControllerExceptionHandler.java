package com.cn.boot.sample.wechat.config;

import cn.hutool.core.exceptions.ExceptionUtil;
import com.cn.boot.sample.api.exceptions.BusinessException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.List;


/**
 * 控制器异常处理
 *
 * @author Chen Nan
 */
@SuppressWarnings("unchecked")
@ControllerAdvice
@Slf4j
public class ControllerExceptionHandler extends HttpStatusHandler {

    /**
     * 控制器异常处理入口
     *
     * @param e 异常信息
     */
    @ExceptionHandler(Throwable.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ResponseBody
    public Object resolveException(Exception e) {
        if (e instanceof BindException) {
            log.error("【参数校验异常】:" + e);
            BindException exs = (BindException) e;
            return error(getArgumentError(exs.getFieldErrors()));
        } else if (e instanceof MethodArgumentNotValidException) {
            log.error("【参数校验异常】:" + e);
            MethodArgumentNotValidException exs = (MethodArgumentNotValidException) e;
            return error(getArgumentError(exs.getBindingResult().getFieldErrors()));
        } else if (ExceptionUtil.isCausedBy(e, BusinessException.class)) {
            log.error("【业务异常】", e);
            BusinessException exception = (BusinessException) ExceptionUtil.getCausedBy(e, BusinessException.class);
            return error(HttpStatus.BAD_REQUEST, exception.getCode(), exception.getMsg());
        }
        log.error(e.getMessage(), e);
        return error(HttpStatus.INTERNAL_SERVER_ERROR, "服务器开小差");
    }

    /**
     * 获取参数校验异常原因
     */
    private String getArgumentError(List<FieldError> errors) {
        if (errors.size() > 0) {
            FieldError error = errors.get(0);
            return error.getField() + error.getDefaultMessage();
        }
        return "参数校验异常";
    }
}
