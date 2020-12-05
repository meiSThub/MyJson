package com.mei.myjson.serializer;

import com.mei.myjson.JsonConfig;
import com.mei.myjson.ObjectSerializer;
import com.mei.myjson.utils.Utils;

import java.util.List;

/**
 * @author mxb
 * @date 2020/12/5
 * @desc List集合序列化器
 * @desired
 */
public class ListSerializer implements ObjectSerializer {

    @Override
    public void serializer(JsonConfig config, StringBuilder out, Object object) {
        // [{"name":"mike"}]
        List<?> list = (List) object;

        // 如果集合为空，则拼接一个空数组："[]"
        if (list == null || list.isEmpty()) {
            out.append("[]");
            return;
        }

        out.append("[");

        for (int i = 0; i < list.size(); i++) {
            if (i != 0) {
                out.append(",");
            }
            Object item = list.get(i);
            if (item == null) {
                out.append("null");
            } else {
                // 获取item的类型
                Class<?> clazz = item.getClass();
                // 如果是基本数据类型
                if (Utils.isBox(clazz) || clazz.isPrimitive()) {
                    out.append(object);
                } else if (Utils.isString(clazz)) { // 如果是字符串类型
                    out.append("\"");
                    out.append(object);
                    out.append("\"");
                } else { // 对象类型
                    ObjectSerializer serializer = config.getSerializer(clazz);
                    serializer.serializer(config, out, item);
                }
            }
        }
        out.append("]");
    }
}
