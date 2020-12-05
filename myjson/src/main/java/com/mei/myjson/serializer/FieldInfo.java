package com.mei.myjson.serializer;

import com.mei.myjson.utils.Log;
import com.mei.myjson.utils.Utils;


import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

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

    public FieldInfo(String name, Field field, Method method) {
        this.name = name;
        this.field = field;
        this.method = method;
        // 获取字段的类型
        type = method != null ? method.getReturnType() : field.getType();
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
