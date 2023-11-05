package com.cn.boot.sample.guava.io;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Chen Nan
 */
@NoArgsConstructor
@Data
public class LogLine {
    /**
     * line : 112.101.154.124 - wechat_mini_cnte [22/Oct/2023:18:52:39 +0000] "POST /api/bmp-charge-app/customer/amount/refund HTTP/1.1" 200 89 "https://servicewechat.com/wx7bb3d7b6b4490c0c/40/page-frame.html" "Mozilla/5.0 (iPad; CPU OS 17_0_2 like Mac OS X) AppleWebKit/605.1.15 (KHTML, like Gecko) Mobile/15E148 MicroMessenger/8.0.42(0x18002a2e) NetType/WIFI Language/zh_CN" 1229 7.706 [bmp-backend-bmp-gateway-8003] [] 10.100.0.20:8003 89 7.706 200 82e960cfeac3a7ae9202803e9cd208c5
     * timestamp : 1698000759996518230
     * fields : {"Line":"112.101.154.124 - wechat_mini_cnte [22/Oct/2023:18:52:39 +0000] \"POST /api/bmp-charge-app/customer/amount/refund HTTP/1.1\" 200 89 \"https://servicewechat.com/wx7bb3d7b6b4490c0c/40/page-frame.html\" \"Mozilla/5.0 (iPad; CPU OS 17_0_2 like Mac OS X) AppleWebKit/605.1.15 (KHTML, like Gecko) Mobile/15E148 MicroMessenger/8.0.42(0x18002a2e) NetType/WIFI Language/zh_CN\" 1229 7.706 [bmp-backend-bmp-gateway-8003] [] 10.100.0.20:8003 89 7.706 200 82e960cfeac3a7ae9202803e9cd208c5\n","app":"ingress-nginx","body_bytes_sent":"89","component":"controller","container":"controller","filename":"/var/log/pods/secp-access_ingress-nginx-controller-56d5d85d8f-58f9k_ec0a7822-4e9a-49a1-b385-232f3a06aa17/controller/0.log","job":"secp-access/ingress-nginx","namespace":"secp-access","node_name":"192.168.0.200","pod":"ingress-nginx-controller-56d5d85d8f-58f9k","remote_addr":"112.101.154.124","remote_user":"wechat_mini_cnte","request_length":"1229","request_time":"7.706","request_uri":"POST /api/bmp-charge-app/customer/amount/refund HTTP/1.1","status":"200","stream":"stdout","time_local":"22/Oct/2023:18:52:39 +0000"}
     */

    private String line;
    private String timestamp;
    private FieldsBean fields;

    @NoArgsConstructor
    @Data
    public static class FieldsBean {
        /**
         * Line : 112.101.154.124 - wechat_mini_cnte [22/Oct/2023:18:52:39 +0000] "POST /api/bmp-charge-app/customer/amount/refund HTTP/1.1" 200 89 "https://servicewechat.com/wx7bb3d7b6b4490c0c/40/page-frame.html" "Mozilla/5.0 (iPad; CPU OS 17_0_2 like Mac OS X) AppleWebKit/605.1.15 (KHTML, like Gecko) Mobile/15E148 MicroMessenger/8.0.42(0x18002a2e) NetType/WIFI Language/zh_CN" 1229 7.706 [bmp-backend-bmp-gateway-8003] [] 10.100.0.20:8003 89 7.706 200 82e960cfeac3a7ae9202803e9cd208c5
         * app : ingress-nginx
         * body_bytes_sent : 89
         * component : controller
         * container : controller
         * filename : /var/log/pods/secp-access_ingress-nginx-controller-56d5d85d8f-58f9k_ec0a7822-4e9a-49a1-b385-232f3a06aa17/controller/0.log
         * job : secp-access/ingress-nginx
         * namespace : secp-access
         * node_name : 192.168.0.200
         * pod : ingress-nginx-controller-56d5d85d8f-58f9k
         * remote_addr : 112.101.154.124
         * remote_user : wechat_mini_cnte
         * request_length : 1229
         * request_time : 7.706
         * request_uri : POST /api/bmp-charge-app/customer/amount/refund HTTP/1.1
         * status : 200
         * stream : stdout
         * time_local : 22/Oct/2023:18:52:39 +0000
         */

        private String Line;
        private String app;
        private String body_bytes_sent;
        private String component;
        private String container;
        private String filename;
        private String job;
        private String namespace;
        private String node_name;
        private String pod;
        private String remote_addr;
        private String remote_user;
        private String request_length;
        private String request_time;
        private String request_uri;
        private String status;
        private String stream;
        private String time_local;
    }
}
