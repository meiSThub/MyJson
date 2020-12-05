package com.mei.myjson;

import com.mei.myjson.serializer.JavaBeanSerializer;
import com.mei.myjson.serializer.ListSerializer;

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

    //
    private Map<Class, ObjectSerializer> serializers = new HashMap<>();

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

}
