package com.mei.myjson;

import com.mei.myjson.deserializer.ObjectDeserializer;
import com.mei.myjson.serializer.ObjectSerializer;

import java.lang.reflect.Type;

/**
 * @author mxb
 * @date 2020/12/5
 * @desc json解析和封装入口类
 * @desired
 */
public class Json {

    /**
     * 把JavaBean转换成Json字符串
     *
     * @param object JavaBean对象
     * @return json字符串
     */
    public static <T> String toJsonString(T object) {
        JsonConfig config = JsonConfig.getGlobalInstance();
        ObjectSerializer serializer = config.getSerializer(object.getClass());
        StringBuilder out = new StringBuilder();
        serializer.serializer(config, out, object);
        return out.toString();
    }


    /**
     * 解析json字符串成一个JavaBean对象
     *
     * @param json  json字符串
     * @param clazz 需要转换的JavaBean类型
     * @return JavaBean对象
     */
    public static <T> T parse(String json, Class<T> clazz) {
        return parse(json, (Type) clazz);
    }

    /**
     * 解析json字符串成一个JavaBean对象
     *
     * @param json json字符串
     * @param type 需要转换的JavaBean类型
     * @return JavaBean对象
     */
    public static <T> T parse(String json, Type type) {
        JsonConfig config = JsonConfig.getGlobalInstance();
        ObjectDeserializer deserializer = config.getDeserializer(type);
        try {
            return deserializer.deserializer(config, json, null);
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }
        return null;
    }
}
