package com.mei.myjson;


import com.mei.myjson.test.Child;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

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

        Object object = Json.parse(s, new TypeReference<List<List<Child>>>() {
        }.getType());
        System.out.println(object);
    }

    @Test
    public void parseJson() {
        String json = "[\n"
                + "\t[{\n"
                + "\t\t\"name\": \"T1\",\n"
                + "\t\t\"test\": 1,\n"
                + "\t\t\"age\": 100,\n"
                + "\t\t\"list\": [\"1\", \"2\"]\n"
                + "\t}, {\n"
                + "\t\t\"name\": \"T2\",\n"
                + "\t\t\"test\": 1,\n"
                + "\t\t\"age\": 200,\n"
                + "\t\t\"list\": [\"1\", \"2\"]\n"
                + "\t}],\n"
                + "\t[{\n"
                + "\t\t\"name\": \"T3\",\n"
                + "\t\t\"test\": 1,\n"
                + "\t\t\"age\": 300,\n"
                + "\t\t\"list\": [\"1\", \"2\"],\n"
                + "\t\t\"childs\": [{\n"
                + "\t\t\t\"name\": \"T3_1\",\n"
                + "\t\t\t\"test\": 1,\n"
                + "\t\t\t\"age\": 3100,\n"
                + "\t\t\t\"list\": [\"1\", \"2\"]\n"
                + "\t\t}, {\n"
                + "\t\t\t\"name\": \"T3_2\",\n"
                + "\t\t\t\"test\": 1,\n"
                + "\t\t\t\"age\": 3200,\n"
                + "\t\t\t\"list\": [\"1\", \"2\"]\n"
                + "\t\t}]\n"
                + "\t}, {\n"
                + "\t\t\"name\": \"T4\",\n"
                + "\t\t\"test\": 1,\n"
                + "\t\t\"age\": 400,\n"
                + "\t\t\"list\": [\"1\", \"2\"]\n"
                + "\t}]\n"
                + "]";

        List<List<Child>> object = Json.parse(json, new TypeReference<List<List<Child>>>() {
        }.getType());

        System.out.println(object);
    }
}