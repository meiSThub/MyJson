package com.mei.myjson.serializer;

import com.mei.myjson.JsonConfig;
import com.mei.myjson.ObjectSerializer;
import com.mei.myjson.utils.Utils;

/**
 * @author mxb
 * @date 2020/12/5
 * @desc JavaBean的属性字段序列化器
 * @desired
 */
public class FieldSerializer {

    private FieldInfo fieldInfo;

    private String key;

    private boolean isPrimitive;

    public FieldSerializer(FieldInfo fieldInfo) {
        this.fieldInfo = fieldInfo;
        // 根据字段名，拼接成json的key值，如："name":
        this.key = '"' + fieldInfo.name + "\":";
        // 是否是基本数据类型或者他的包装类
        isPrimitive = fieldInfo.isPrimitive();
    }

    /**
     * 序列化字段
     *
     * @param config 序列化全局配置
     * @param object JavaBean对象
     * @return 输出属性的json字符串格式，如："name":"tom"
     */
    public String serializer(JsonConfig config, Object object) {
        // 1. 获取字段的值
        Object filedValue = fieldInfo.get(object);
        if (filedValue == null) {
            return ""; // 字段值为空，则返回一个空字符串：""
        }
        StringBuilder sb = new StringBuilder();

        // 2. 当前序列化的字段，是否是基本数据类型
        if (isPrimitive) {// 基本数据类型，直接拼接
            sb.append(key);
            sb.append(filedValue);
        } else if (Utils.isString(fieldInfo.type)) {
            // 字符串类型，拼接value时，需要加上双引号
            sb.append(key);
            sb.append("\"");
            sb.append(filedValue);
            sb.append("\"");
        } else {
            // 如果是对象类型，则根据对象type获取到合适的对象序列化器
            ObjectSerializer serializer = config.getSerializer(fieldInfo.type);
            sb.append(key);
            serializer.serializer(config, sb, filedValue);
        }

        return sb.toString();
    }
}
