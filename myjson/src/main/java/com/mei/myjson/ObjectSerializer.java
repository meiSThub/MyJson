package com.mei.myjson;

/**
 * @author mxb
 * @date 2020/12/5
 * @desc 对象序列化接口，所有的序列化器都实现该接口
 * @desired
 */
public interface ObjectSerializer {

    /**
     * 序列化接口
     *
     * @param config 全局配置对象
     * @param out    输出的Json字符串构建者
     * @param object 需要转换成json字符串的javabean对象
     */
    void serializer(JsonConfig config, StringBuilder out, Object object);
}
