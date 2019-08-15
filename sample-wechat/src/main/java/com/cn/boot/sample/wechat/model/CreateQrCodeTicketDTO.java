package com.cn.boot.sample.wechat.model;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Chen Nan
 */
@NoArgsConstructor
@Data
public class CreateQrCodeTicketDTO {
    /**
     * expire_seconds : 604800
     * action_name : QR_STR_SCENE
     * action_info : {"scene":{"scene_str":"test"}}
     */

    private int expire_seconds;
    private String action_name;
    private ActionInfoBean action_info;

    @NoArgsConstructor
    @Data
    public static class ActionInfoBean {
        /**
         * scene : {"scene_str":"test"}
         */

        private SceneBean scene;

        @NoArgsConstructor
        @Data
        public static class SceneBean {
            /**
             * scene_str : test
             */

            private String scene_str;
        }
    }
}
