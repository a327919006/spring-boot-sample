package com.cn.boot.sample.design.pattern.prototype;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * 原型设计模式
 *
 * @author Chen Nan
 */
public class Person implements Cloneable, Serializable {
    private String name;
    private Integer age;
    private List<String> list = new ArrayList<>();

    public Person() {
        System.out.println("构造函数调用");
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public List<String> getList() {
        return list;
    }

    public void setList(List<String> list) {
        this.list = list;
    }

    /**
     * 浅拷贝，通过实现Cloneable接口
     */
    @Override
    protected Person clone() throws CloneNotSupportedException {
        return (Person) super.clone();
    }

    /**
     * 深拷贝
     */
    public Person deepClone() {
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(baos);
            oos.writeObject(this);

            ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());
            ObjectInputStream ois = new ObjectInputStream(bais);

            return (Person) ois.readObject();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public String toString() {
        return "Person{" +
                "name='" + name + '\'' +
                ", age=" + age +
                ", list=" + list +
                '}';
    }
}
