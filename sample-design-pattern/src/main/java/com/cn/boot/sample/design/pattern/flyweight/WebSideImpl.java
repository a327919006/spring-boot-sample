package com.cn.boot.sample.design.pattern.flyweight;

/**
 * @author Chen Nan
 */
public class WebSideImpl implements WebSide {
    private String category;

    public WebSideImpl(String category) {
        this.category = category;
    }

    @Override
    public void run(Company company) {
        System.out.println("公司名称:" + company.getName() + ", 网站类型:" + category);
    }
}
