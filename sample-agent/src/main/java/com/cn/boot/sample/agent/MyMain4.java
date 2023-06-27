package com.cn.boot.sample.agent;

import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtMethod;

import java.lang.reflect.Method;

/**
 * 新建class和method
 *
 * @author Chen Nan
 */
public class MyMain4 {

    public static void main(String[] args) {
        try {
            ClassPool pool = ClassPool.getDefault();
            CtClass myClass = pool.makeClass("com.cn.boot.sample.agent.MyTest");
            CtMethod myMethod = CtMethod.make("    public String parse(Integer id) {\n" +
                    "        System.out.println(\"处理过程 id=\" + id);\n" +
                    "        return \"处理结果\";\n" +
                    "    }", myClass);
            myClass.addMethod(myMethod);

            Class clazz = myClass.toClass();
            Object obj = clazz.newInstance(); // 通过调用User 无参构造函数
            Method method = clazz.getDeclaredMethod("parse", Integer.class);
            Object result = method.invoke(obj, 200);
            System.out.println(result);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
