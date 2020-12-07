package com.mei.myjson.deserializer;

import com.mei.myjson.JsonConfig;
import com.mei.myjson.FieldInfo;
import com.mei.myjson.utils.Utils;

import org.json.JSONArray;
import org.json.JSONObject;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;

/**
 * @author mxb
 * @date 2020/12/6
 * @desc JavaBean 反序列化器，即把Json字符串，转换成JavaBean对象
 * @desired
 */
public class JavaBeanDeserializer implements ObjectDeserializer {

    // 需要转换成JavaBean类型
    private Class beanType;

    // 采集到的可以被赋值的成员属性
    private final List<FieldInfo> fieldInfos;

    public JavaBeanDeserializer(Class beanType) {
        this.beanType = beanType;
        Map<String, Field> fieldMap = Utils.parseAllFieldToCache(beanType);
        fieldInfos = Utils.computeSetter(beanType, fieldMap);
    }

    @Override
    public <T> T deserializer(JsonConfig config, String json, Object object) throws Throwable {
        JSONObject jsonObject;
        if (object == null) {
            jsonObject = new JSONObject(json);
        } else {
            jsonObject = (JSONObject) object;
        }

        // 根据JavaBean类型，反射创建一个JavaBean对象
        T bean = null;
        try {
            bean = (T) beanType.newInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }

        // 遍历采集到的成员属性，根据属性名，从json字符串中取值，并为属性赋值
        for (FieldInfo fieldInfo : fieldInfos) {
            // 根据字段名，从Json对象中获取值
            Object value = jsonObject.opt(fieldInfo.name);
            if (value == null) {
                continue;
            }

            // 如果获取到的是一个JSONObject或者JSONArray对象，则
            if (value instanceof JSONObject || value instanceof JSONArray) {
                ObjectDeserializer deserializer = config.getDeserializer(fieldInfo.genericType);
                // 反序列化出的对象
                Object obj = deserializer.deserializer(config, null, value);
                // 为字段赋值
                fieldInfo.set(bean, obj);
            } else {
                if (value != JSONObject.NULL) {
                    fieldInfo.set(bean, value);
                }
            }
        }

        return bean;
    }
}
