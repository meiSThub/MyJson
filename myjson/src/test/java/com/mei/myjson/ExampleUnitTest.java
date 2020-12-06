package com.mei.myjson;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {

    @Test
    public void addition_isCorrect() {
        List<List<Child>> childLists = new ArrayList<>();
        List<Child> child1 = new ArrayList<>();
        child1.add(new Child("T1", 100));
        child1.add(new Child("T2", 200));
        childLists.add(child1);
        List<Child> child2 = new ArrayList<>();
        Child t3 = new Child("T3", 300);
        t3.childs = new ArrayList<>();
        t3.childs.add(new Child("T3_1", 3100));
        t3.childs.add(new Child("T3_2", 3200));
        child2.add(t3);
        child2.add(new Child("T4", 400));
        childLists.add(child2);

        String s = Json.toJsonString(childLists);
        System.out.println(s);

        // Object object = com.alibaba.fastjson.JSON.parseObject(s, new com.alibaba.fastjson.TypeReference<List<List<Child>>>() {
        // }.getType());
        // System.out.println(object);
    }

    public class B {

        public A a;

    }

    public class A {

        public List<B> b;

        public B b1;

    }

    @Test
    public void addition_isCorrect1() throws Exception {
        A a = new A();
        a.b1 = new B();
        a.b1.a = a;
        String s = Json.toJsonString(a);
        System.out.println(s);
    }
}