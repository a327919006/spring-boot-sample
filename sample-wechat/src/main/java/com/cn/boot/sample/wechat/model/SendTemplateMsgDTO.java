package com.cn.boot.sample.wechat.model;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Chen Nan
 */
@NoArgsConstructor
@Data
public class SendTemplateMsgDTO {
    /**
     * touser : OPENID
     * template_id : ngqIpbwh8bUfcSsECmogfXcV14J0tQlEpBO27izEYtY
     * url : http://weixin.qq.com/download
     * miniprogram : {"appid":"xiaochengxuappid12345","pagepath":"index?foo=bar"}
     * data : {"first":{"value":"恭喜你购买成功！","color":"#173177"},"keyword1":{"value":"巧克力","color":"#173177"},"keyword2":{"value":"39.8元","color":"#173177"},"keyword3":{"value":"2014年9月22日","color":"#173177"},"remark":{"value":"欢迎再次购买！","color":"#173177"}}
     */

    private String touser;
    private String template_id;
    private String url;
    private DataBean data;

    @NoArgsConstructor
    @Data
    public static class DataBean {
        /**
         * first : {"value":"恭喜你购买成功！","color":"#173177"}
         * keyword1 : {"value":"巧克力","color":"#173177"}
         * keyword2 : {"value":"39.8元","color":"#173177"}
         * keyword3 : {"value":"2014年9月22日","color":"#173177"}
         * remark : {"value":"欢迎再次购买！","color":"#173177"}
         */

        private FirstBean first;
        private Keyword1Bean keyword1;
        private Keyword2Bean keyword2;
        private Keyword3Bean keyword3;
        private RemarkBean remark;

        @NoArgsConstructor
        @Data
        public static class FirstBean {
            /**
             * value : 恭喜你购买成功！
             * color : #173177
             */

            private String value;
            private String color;
        }

        @NoArgsConstructor
        @Data
        public static class Keyword1Bean {
            /**
             * value : 巧克力
             * color : #173177
             */

            private String value;
            private String color;
        }

        @NoArgsConstructor
        @Data
        public static class Keyword2Bean {
            /**
             * value : 39.8元
             * color : #173177
             */

            private String value;
            private String color;
        }

        @NoArgsConstructor
        @Data
        public static class Keyword3Bean {
            /**
             * value : 2014年9月22日
             * color : #173177
             */

            private String value;
            private String color;
        }

        @NoArgsConstructor
        @Data
        public static class RemarkBean {
            /**
             * value : 欢迎再次购买！
             * color : #173177
             */

            private String value;
            private String color;
        }
    }
}
