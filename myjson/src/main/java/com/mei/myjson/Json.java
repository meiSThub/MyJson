package com.mei.myjson;

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
}
