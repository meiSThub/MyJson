package com.mei.myjson.deserializer;

import com.mei.myjson.JsonConfig;
import com.mei.myjson.utils.Utils;

import org.json.JSONArray;
import org.json.JSONObject;

import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.List;

/**
 * @author mxb
 * @date 2020/12/6
 * @desc List集合类型的反序列化器，即根据json字符串，创建一个List集合对象
 * @desired
 */
public class ListDeserializer implements ObjectDeserializer {

    private ParameterizedType type;

    public ListDeserializer(ParameterizedType type) {
        this.type = type;
    }

    @Override
    public <T> T deserializer(JsonConfig config, String json, Object object) throws Throwable {
        JSONArray jsonArray;
        if (object == null) {
            jsonArray = new JSONArray(json);
        } else {
            jsonArray = (JSONArray) object;
        }

        // 创建一个List对象
        List list = new ArrayList<>();

        // 遍历JSONArray数组
        for (int i = 0; i < jsonArray.length(); i++) {
            Object itemValue = jsonArray.opt(i);
            if (itemValue instanceof JSONObject || itemValue instanceof JSONArray) {
                ObjectDeserializer deserializer = config.getDeserializer(Utils.getItemType(type));
                Object obj = deserializer.deserializer(config, null, itemValue);
                list.add(obj);
            } else { // 普通类型和字符串类型
                list.add(itemValue);
            }
        }

        return (T) list;
    }
}
