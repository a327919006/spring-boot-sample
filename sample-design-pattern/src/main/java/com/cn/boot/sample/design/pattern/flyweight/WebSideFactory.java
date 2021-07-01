package com.cn.boot.sample.design.pattern.flyweight;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Chen Nan
 */
public class WebSideFactory {

    private Map<String, WebSide> webSideMap = new HashMap<>();

    public WebSide getWebSize(String category) {
        if (!webSideMap.containsKey(category)) {
            WebSideImpl webSide = new WebSideImpl(category);
            webSideMap.put(category, webSide);
        }
        return webSideMap.get(category);
    }

    public int getSize() {
        return webSideMap.size();
    }
}
