package com.cn.boot.sample.design.pattern.prototype;

/**
 * @author Chen Nan
 */
public class PrototypeTest {

    public static void main(String[] args) throws CloneNotSupportedException {
        Person person = new Person();
        person.setName("test1");
        person.setAge(10);
        person.getList().add("aaa");

        // 浅拷贝存在问题，因为list非基础类型，只会拷贝地址，因此拷贝的对象与原对象的list指向同一个实例，会互相影响
//        Person clone = person.clone();
//        clone.setAge(11);
//        clone.getList().add("bbb");

        Person deepClone = person.deepClone();
        deepClone.setAge(12);
        deepClone.getList().add("ccc");

        System.out.println(person);
//        System.out.println(clone);
        System.out.println(deepClone);
    }
}
