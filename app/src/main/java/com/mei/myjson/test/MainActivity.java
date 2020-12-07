package com.mei.myjson.test;

import com.mei.myjson.Json;
import com.mei.myjson.TypeReference;
import com.mei.myjson.test.Child;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    TextView mTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mTextView = findViewById(R.id.text_view);

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

        mTextView.setText(s + "\n\n\n" + object);
    }
}