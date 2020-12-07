package com.mei.myjson.test;

/**
 * @author mxb
 * @date 2020/12/6
 * @desc
 * @desired
 */
public class Parent {

    String name;

    public Parent() {
    }

    public Parent(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }


    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "Parent{" +
                "name='" + name + '\'' +
                '}';
    }
}