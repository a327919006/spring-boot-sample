package com.cn.boot.sample.agent;

/**
 * @author Chen Nan
 */
public class MyMain {

    public static void main(String[] args) {
        User user = new User(1, "test1");
        String schoolName = user.getSchoolName(1);
        System.out.println("school=" + schoolName);
    }
}
