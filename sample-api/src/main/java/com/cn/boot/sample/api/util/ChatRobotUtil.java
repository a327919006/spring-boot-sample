package com.cn.boot.sample.api.util;

import cn.hutool.http.HttpRequest;
import cn.hutool.json.JSONUtil;
import com.cn.boot.sample.api.model.dto.chat.ChatRsp;

/**
 * @author Chen Nan
 */
public class ChatRobotUtil {
    private static final String URL = "http://api.qingyunke.com/api.php?key=free&appid=0&msg=";

    public static String chat(String content) {
        String result = HttpRequest.get(URL + content).execute().body();
        ChatRsp chatRsp = JSONUtil.toBean(result, ChatRsp.class);
        if (chatRsp.getResult() == 0 && chatRsp.getContent().length() < 200) {
            return chatRsp.getContent().replaceAll("\\{br}", "\n");
        }
        return "";
    }
}
