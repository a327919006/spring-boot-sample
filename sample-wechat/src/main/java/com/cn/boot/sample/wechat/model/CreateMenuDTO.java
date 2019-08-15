package com.cn.boot.sample.wechat.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author Chen Nan
 */
@NoArgsConstructor
@Data
public class CreateMenuDTO {
    private List<ButtonBean> button;

    @NoArgsConstructor
    @Data
    public static class ButtonBean {
        /**
         * type : click
         * name : 今日歌曲
         * key : V1001_TODAY_MUSIC
         * sub_button : [{"type":"view","name":"搜索","url":"http://www.soso.com/"},{"type":"miniprogram","name":"wxa","url":"http://mp.weixin.qq.com","appid":"wx286b93c14bbf93aa","pagepath":"pages/lunar/index"},{"type":"click","name":"赞一下我们","key":"V1001_GOOD"}]
         */

        private String type;
        private String name;
        private String key;
        private String media_id;
        private List<SubButtonBean> sub_button;

        @NoArgsConstructor
        @Data
        public static class SubButtonBean {
            /**
             * type : view
             * name : 搜索
             * url : http://www.soso.com/
             * appid : wx286b93c14bbf93aa
             * pagepath : pages/lunar/index
             * key : V1001_GOOD
             */

            private String type;
            private String name;
            private String url;
            private String appid;
            private String pagepath;
            private String key;
            private String media_id;
        }
    }
}
