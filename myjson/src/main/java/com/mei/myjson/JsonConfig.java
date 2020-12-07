package com.mei.myjson;

import com.mei.myjson.deserializer.JavaBeanDeserializer;
import com.mei.myjson.deserializer.ListDeserializer;
import com.mei.myjson.deserializer.ObjectDeserializer;
import com.mei.myjson.serializer.JavaBeanSerializer;
import com.mei.myjson.serializer.ListSerializer;
import com.mei.myjson.serializer.ObjectSerializer;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author mxb
 * @date 2020/12/5
 * @desc json配置器
 * @desired
 */
public class JsonConfig {

    private static JsonConfig globalInstance = new JsonConfig();

    // 序列化器集合
    private Map<Class, ObjectSerializer> serializers = new HashMap<>();

    // 反序列化器集合
    private Map<Type, ObjectDeserializer> deserializers = new HashMap<>();

    public static JsonConfig getGlobalInstance() {
        return globalInstance;
    }

    /**
     * 根据给定的类类型，查找一个具体的序列化器，用于序列化指定的对象
     *
     * @param clazz 类类型
     * @return 序列化器
     */
    public ObjectSerializer getSerializer(Class<?> clazz) {
        ObjectSerializer objectSerializer = serializers.get(clazz);
        // 如果缓存中有，则直接用缓存中的对象序列化器
        if (objectSerializer != null) {
            return objectSerializer;
        }

        // 如果是List集合类型
        if (List.class.isAssignableFrom(clazz)) {
            objectSerializer = new ListSerializer();
        } else if (Map.class.isAssignableFrom(clazz)) {// 如果是map集合
            // todo map等待实现
        } else if (clazz.isArray()) {// 如果是数组类型
            // todo 数组序列化器等待实现
        } else {
            objectSerializer = new JavaBeanSerializer(clazz);
        }
        serializers.put(clazz, objectSerializer);
        return objectSerializer;
    }

    /**
     * 根据给定的类型，获取反序列化器
     *
     * @param type 类型
     * @return 反序列化器
     */
    public ObjectDeserializer getDeserializer(Type type) {
        ObjectDeserializer objectDeserializer = deserializers.get(type);
        if (objectDeserializer != null) {
            return objectDeserializer;
        }

        if (type instanceof Class) {
            objectDeserializer = new JavaBeanDeserializer((Class<?>) type);
        } else if (type instanceof ParameterizedType) {
            // List<Child>
            objectDeserializer = new ListDeserializer((ParameterizedType) type);
        }
        return objectDeserializer;
    }
}
