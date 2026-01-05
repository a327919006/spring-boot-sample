package com.cn.boot.sample.server.service;

import javax.servlet.http.HttpServletResponse;

/**
 * @author Chen Nan
 */
public interface TestService {
    void listByHandler();

    void exportCsv(HttpServletResponse response);

    void exportExcel(HttpServletResponse response);
}
