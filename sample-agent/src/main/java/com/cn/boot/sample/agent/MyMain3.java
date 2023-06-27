package com.cn.boot.sample.agent;

import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtMethod;

import java.lang.reflect.Method;

/**
 * 修改原有方法内的业务代码
 *
 * @author Chen Nan
 */
public class MyMain3 {

    public static void main(String[] args) {
        String modifyClassName = "com.cn.boot.sample.agent.User";
        String modifyClassMethod = "getSchoolName";
        // 开始使用当前的javassist修改字节码文件
        try {
            ClassPool pool = ClassPool.getDefault();
            CtClass cc = pool.get(modifyClassName);
            CtMethod declaredMethod = cc.getDeclaredMethod(modifyClassMethod);
            // $1表示的就是第一个参数id

            declaredMethod.setBody("{System.out.println(\"hello insert\");"
//                    + "$1=Integer.valueOf(777);"
                    + "System.out.println(\"当前id=\" + $1);" +
                    "return \"A中学\";}");
//            cc.detach();

            Class clazz = cc.toClass();
            Object obj = clazz.newInstance(); // 通过调用User 无参构造函数
            Method method = clazz.getDeclaredMethod(modifyClassMethod, Integer.class);
            Object result = method.invoke(obj, 200);
            System.out.println(result);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
