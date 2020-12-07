package com.mei.myjson.test;

/**
 * @author mxb
 * @date 2020/12/6
 * @desc
 * @desired
 */

import java.util.ArrayList;
import java.util.List;

/**
 * @author mxb
 * @date 2020/12/5
 * @desc
 * @desired
 */
public class Child extends Parent {

    private int age;

    public List<String> list;

    public List<Child> childs;

    public Child() {

    }

    /**
     * 指定FastJson反序列化构造函数
     */
    public Child(String name, int age) {
        super(name);
        this.age = age;
        list = new ArrayList<>();
        list.add("1");
        list.add("2");
    }

    public int getTest() {
        return 1;
    }

    //非公有属性需要有
    public void setAge(int age) {
        this.age = age;
    }

    public int getAge() {
        return age;
    }

    @Override
    public String toString() {
        return "Child{" +
                "name='" + name + '\'' +
                ", age=" + age +
                ", list=" + list +
                ", childs=" + childs +
                '}';
    }
}
