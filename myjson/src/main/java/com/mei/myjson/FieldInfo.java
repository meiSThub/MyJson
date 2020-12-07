package com.mei.myjson;

import com.mei.myjson.utils.Log;
import com.mei.myjson.utils.Utils;


import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Type;

/**
 * @author mxb
 * @date 2020/12/5
 * @desc 可以被序列化的成员属性 封装类
 * @desired
 */
public class FieldInfo {

    private static final String TAG = "FieldInfo";

    /**
     * 字段名
     */
    public String name;

    /**
     * 字段对象
     */
    public Field field;

    /**
     * 字段对应的setter/getter方法
     */
    public Method method;

    /**
     * 字段的类型
     */
    public Class<?> type;

    /**
     * 参数化类型
     */
    public Type genericType;

    public FieldInfo(String name, Field field, Method method) {
        this(name, field, method, false);
    }

    /**
     * 封装字段相关信息
     *
     * @param name     字段名
     * @param field    字段对象
     * @param method   字段对应的setter/getter方法
     * @param isSetter 是否是setter函数
     */
    public FieldInfo(String name, Field field, Method method, boolean isSetter) {
        this.name = name;
        this.field = field;
        this.method = method;
        // 获取字段的类型
        type = method != null ? method.getReturnType() : field.getType();
        if (isSetter) {
            // 当我们采集set函数的时候，实际上已经过滤了 有且只有一个参数的函数
            if (method != null) {
                // 所以这里一定是可以拿到参数类型的
                genericType = method.getGenericParameterTypes()[0];
            } else {
                // 如果没有对应的setter方法，则直接取字段的类型
                genericType = field.getGenericType();
            }
        }
    }

    public FieldInfo(String name, Field field, Method method, Class<?> type) {
        this.name = name;
        this.field = field;
        this.method = method;
        this.type = type;
    }

    /**
     * 获取字段的值
     *
     * @param object JavaBean对象
     * @return 返回JavaBean对象对应的属性的值
     */
    public Object get(Object object) {
        try {
            // 如果有getter方法，则调用getter方法获取，如果没有，说明字段是public类型，直接取
            return method != null ? method.invoke(object) : field.get(object);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 为JavaBean对象的属性赋值
     *
     * @param object JavaBean对象
     * @param value  属性值
     */
    public void set(Object object, Object value) {
        try {
            if (method != null) {
                method.invoke(object, value);
            } else {
                field.set(object, value);
            }
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
    }

    public boolean isPrimitive() {
        boolean isPrimitive = Utils.isBox(type) || type.isPrimitive();
        Log.i(TAG, "isPrimitive: " + isPrimitive);
        return isPrimitive;
    }
}
