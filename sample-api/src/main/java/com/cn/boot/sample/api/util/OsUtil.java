package com.cn.boot.sample.api.util;

import cn.hutool.core.util.StrUtil;
import cn.hutool.system.SystemUtil;

/**
 * @author Chen Nan
 */
public class OsUtil extends SystemUtil {
    public static final String WINDOWS = "windows";

    public static boolean isWindows() {
        String osName = get(OS_NAME);
        if (StrUtil.containsAnyIgnoreCase(osName, WINDOWS)) {
            return true;
        }
        return false;
    }
}
