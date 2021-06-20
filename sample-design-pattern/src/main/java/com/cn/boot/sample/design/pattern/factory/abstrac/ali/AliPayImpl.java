package com.cn.boot.sample.design.pattern.factory.abstrac.ali;

import com.cn.boot.sample.design.pattern.factory.abstrac.Pay;

/**
 * @author Chen Nan
 */
public class AliPayImpl implements Pay {
    @Override
    public void pay() {
        System.out.println("ali pay");
    }
}
