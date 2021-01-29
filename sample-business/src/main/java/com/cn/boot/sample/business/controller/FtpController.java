package com.cn.boot.sample.business.controller;

import com.cn.boot.sample.api.model.Constants;
import com.cn.boot.sample.business.config.properties.TestConfigProperties;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPReply;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.io.IOException;
import java.security.SecureRandom;
import java.security.cert.X509Certificate;

/**
 * @author ChenNan
 */
@Slf4j
@RestController
@RequestMapping("/ftp")
@Api(tags = "测试FtpClient功能", produces = MediaType.APPLICATION_JSON_VALUE)
public class FtpController {

    @Autowired
    private TestConfigProperties config;

    @ApiOperation("加密连接-FTPS，SSL方式，获取文件列表")
    @PostMapping("/ssl/list")
    public String[] list() {
        FTPClient ftpClient = getFtpClient();
        if (ftpClient != null) {
            try {
                String[] names = ftpClient.listNames();
                for (String name : names) {
                    log.info("name = " + name);
                }
                return names;
            } catch (IOException e) {
                log.error("【FTP】获取文件名称列表异常：", e);
            }
        }
        return new String[]{Constants.MSG_FAIL};
    }

    /**
     * 连接Ftp服务器
     *
     * @return ftp连接
     */
    private FTPClient getFtpClient() {
        try {
            log.info("【FTP】开始连接");

            TrustManager[] trustManager = new TrustManager[]{new X509TrustManager() {
                @Override
                public X509Certificate[] getAcceptedIssuers() {
                    return null;
                }

                @Override
                public void checkClientTrusted(X509Certificate[] certs, String authType) {
                }

                @Override
                public void checkServerTrusted(X509Certificate[] certs, String authType) {
                }
            }};
            SSLContext sslContext = null;
            sslContext = SSLContext.getInstance("SSL");
            sslContext.init(null, trustManager, new SecureRandom());
            SSLSocketFactory sslSocketFactory = sslContext.getSocketFactory();

            FTPClient ftpClient = new FTPClient();
            ftpClient.setSocketFactory(sslSocketFactory);

            ftpClient.connect(config.getFtp().getIp(), config.getFtp().getPort());
            if (ftpClient.isConnected()) {
                if (ftpClient.login(config.getFtp().getUsername(), config.getFtp().getPassword())) {
                    int reply = ftpClient.getReplyCode();
                    log.info("【FTP】状态码:" + reply);
                    if (!FTPReply.isPositiveCompletion(reply)) {
                        ftpClient.disconnect();
                        log.info("【FTP】服务拒绝连接！");
                    } else {
                        ftpClient.setControlEncoding("UTF-8");
                        ftpClient.enterLocalPassiveMode();
                        ftpClient.sendCommand("PROT", "P");
                        log.info("【FTP】连接成功");
                        return ftpClient;
                    }
                } else {
                    log.error("【FTP】账号或密码错误");
                }
            } else {
                log.error("【FTP】连接服务器失败");
            }
        } catch (Exception e) {
            log.error("【FTP】连接异常：", e);
        }
        return null;
    }
}
