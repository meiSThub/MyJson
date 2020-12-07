package com.mei.myjson.deserializer;

import com.mei.myjson.JsonConfig;

/**
 * @author mxb
 * @date 2020/12/6
 * @desc 反序列化器接口定义
 * @desired
 */
public interface ObjectDeserializer {

    /**
     * 反序列化方法，把json字符串转换成指定的JavaBean对象
     *
     * @param config 全局配置
     * @param json   json字符串
     * @param object 由字符串转换成的JsonObject对象
     * @param <T>    JavaBean泛型
     * @return 由Json转换成的一个JavaBean对象
     */
    <T> T deserializer(JsonConfig config, String json, Object object) throws Throwable;
}
