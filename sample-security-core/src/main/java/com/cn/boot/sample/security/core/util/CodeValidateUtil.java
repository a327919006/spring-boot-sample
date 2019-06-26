package com.cn.boot.sample.security.core.util;

import org.apache.commons.lang3.StringUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 * <p>图形验证码工具类</p>
 *
 * @author Chen Nan
 */
public final class CodeValidateUtil {

    private CodeValidateUtil() {
        throw new RuntimeException("CaptchaUtil.class can't be instantiated");
    }

    public static final String SESSION_KEY = "validate_code";
    public static final String SESSION_TIME_KEY = "validate_code_time";
    /**
     * 设置验证码的有效时间 3分钟
     */
    private static final long TIMEOUT = 1000 * 60 * 3;

    /**
     * <p>验证码合法性校验</p>
     *
     * @param request     请求
     * @param userCaptcha 用户输入的验证码
     * @param expire      超时时长
     * @return 验证码校验结果
     */
    public static boolean validate(HttpServletRequest request, String userCaptcha, long expire) {
        // 验证参数
        if (null == request || StringUtils.isEmpty(userCaptcha)) {
            return false;
        }

        // 获取session
        HttpSession session = request.getSession(false);
        if (session == null) {
            return false;
        }

        Object captcha = session.getAttribute(SESSION_KEY);
        if (captcha != null && userCaptcha.equalsIgnoreCase(captcha.toString())) {
            long timestamp = (Long) session.getAttribute(SESSION_TIME_KEY);
            // 判断是否过期
            if ((timestamp + expire) > System.currentTimeMillis()) {
                session.removeAttribute(SESSION_KEY);
                session.removeAttribute(SESSION_TIME_KEY);
                return true;
            }
        }

        return false;
    }

    /**
     * <p>验证验证码是否正确</p>
     *
     * @param request 请求
     * @return 验证码校验结果
     */
    public static boolean validate(HttpServletRequest request, String userCaptcha) {
        return validate(request, userCaptcha, TIMEOUT);
    }

    /**
     * <p>验证验证码是否正确</p>
     *
     * @param request 请求
     * @return 验证码校验结果
     */
    public static boolean validate(HttpServletRequest request) {
        String userCaptcha = request.getParameter("captcha");
        return validate(request, userCaptcha, TIMEOUT);
    }
}
