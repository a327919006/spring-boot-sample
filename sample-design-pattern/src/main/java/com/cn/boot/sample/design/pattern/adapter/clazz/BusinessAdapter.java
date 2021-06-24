package com.cn.boot.sample.design.pattern.adapter.clazz;

/**
 * @author Chen Nan
 */
public class BusinessAdapter extends OldBusiness implements NewBusiness {
    @Override
    public void businessB() {
        System.out.println("businessB");
    }

    @Override
    public void businessC() {
        System.out.println("businessC");
    }
}
