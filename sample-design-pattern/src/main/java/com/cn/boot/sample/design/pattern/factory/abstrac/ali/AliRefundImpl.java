package com.cn.boot.sample.design.pattern.factory.abstrac.ali;

import com.cn.boot.sample.design.pattern.factory.abstrac.Refund;

/**
 * @author Chen Nan
 */
public class AliRefundImpl implements Refund {
    @Override
    public void refund() {
        System.out.println("ali refund");
    }
}
