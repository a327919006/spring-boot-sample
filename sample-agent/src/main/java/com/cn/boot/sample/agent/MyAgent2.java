package com.cn.boot.sample.agent;

import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtMethod;

import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.IllegalClassFormatException;
import java.lang.instrument.Instrumentation;
import java.security.ProtectionDomain;

/**
 * @author Chen Nan
 */
public class MyAgent2 {

    public static void premain(String agentArgs, Instrumentation instrumentation) {
        System.out.println("我是" + agentArgs);
        MyClassFileTransformer transformer = new MyClassFileTransformer();
        instrumentation.addTransformer(transformer);
    }

    public static class MyClassFileTransformer implements ClassFileTransformer {
        @Override
        public byte[] transform(ClassLoader loader, String className, Class<?> classBeingRedefined,
                                ProtectionDomain protectionDomain, byte[] classfileBuffer) throws IllegalClassFormatException {
            //	开始处理当前User类的getSchoolName方法，让其显示并返回数据
            // System.out.println("已加载：" + className);
            String modifyClassName = "com.cn.boot.sample.agent.User";
            String modifyClassMethod = "getSchoolName";
            String loadClassName = modifyClassName.replace(".", "/");
            // 表示找到了这个类
            if (loadClassName.equals(className)) {
                // 开始使用当前的javassist修改字节码文件
                try {
                    ClassPool pool = ClassPool.getDefault();
                    CtClass cc = pool.get(modifyClassName);
                    CtMethod declaredMethod = cc.getDeclaredMethod(modifyClassMethod);
                    // $1表示的就是第一个参数id

                    declaredMethod.setBody("{System.out.println(\"hello insert\");"
                            + "$1=Integer.valueOf(6666);"
                            + "System.out.println(\"当前id=\" + $1);" +
                            "return \"A中学\";}");
                    cc.detach();
                    return cc.toBytecode();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            return classfileBuffer;
        }
    }
}
