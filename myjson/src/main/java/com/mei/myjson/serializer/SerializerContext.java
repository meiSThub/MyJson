package com.mei.myjson.serializer;

import java.util.HashMap;
import java.util.Map;

/**
 * @author mxb
 * @date 2020/12/6
 * @desc 序列化上下文，用于解决循环引用的问题
 * @desired
 */
public class SerializerContext {

    /**
     * key: 属性名
     * value:属性值
     */
    private Map<String, Object> refrence = new HashMap<>();

    /**
     * 将要被序列化的字段，是否已经被序列化过，
     *
     * @param key   属性名
     * @param value 属性值
     * @return true:相同的属性和值已经被序列化过，false：属性没有被序列化过
     */
    public boolean isSameKeyAndValue(String key, Object value) {
        return refrence.containsKey(key) && refrence.get(key) == value;
    }

    /**
     * 缓存属性名和属性值
     *
     * @param key   属性名
     * @param value 属性值
     */
    public void save(String key, Object value) {
        refrence.put(key, value);
    }
}
