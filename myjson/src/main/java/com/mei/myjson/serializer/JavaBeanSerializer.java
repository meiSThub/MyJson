package com.mei.myjson.serializer;

import com.mei.myjson.JsonConfig;
import com.mei.myjson.utils.Utils;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;

/**
 * @author mxb
 * @date 2020/12/5
 * @desc 序列化JavaBean对象的序列化器
 * 即专门对一个JavaBean对象进行序列化
 * @desired
 */
public class JavaBeanSerializer implements ObjectSerializer {

    /**
     * 当我们想把JavaBean对象转换成一个json字符串的时候，就是把JavaBean对象中的所有属性取出来，并转换成json对象，即序列化过程
     * 针对一个JavaBean对象，哪些属性是需要进行序列化的呢？
     * 1. public 修饰的 属性
     * 2. 有公有的无参的get方法的属性，如：public String getName()
     * 3. 有公有的无参的is方法的属性，如：public boolean isMale(),正对的是boolean类型的属性
     * 4. 采集父类中符合规则：1，2，3的属性
     */


    /**
     * 当前JavaBean对象，所有符合序列化要求的属性 所对应的属性序列化器集合
     */
    private List<FieldSerializer> fieldSerializers;

    /**
     * 构造方法
     *
     * @param beanType 需要进行序列化的JavaBean类型
     */
    public JavaBeanSerializer(Class<?> beanType) {
        // 1. 查找自己与父类 所有符合条件的函数：getXX(),isXX(),
        // 2. 查找自己与父类的public类型的属性

        Map<String, Field> fieldMap = Utils.parseAllFieldToCache(beanType);
        fieldSerializers = Utils.computeGetters(beanType, fieldMap);
    }

    /**
     * 把一个javabean对象序列化成一个json字符串
     *
     * @param config 全局配置对象
     * @param out    输出的Json字符串构建者
     * @param object 需要转换成json字符串的javabean对象
     */
    @Override
    public void serializer(JsonConfig config, StringBuilder out, Object object) {
        // {"age":100,"name":"testname","test":1,"list":["1","2"]}
        out.append("{");
        int i = 0;
        // 遍历字段序列化器，去序列化JavaBean对象符合要求的每一个属性
        for (FieldSerializer fieldSerializer : fieldSerializers) {
            if (i != 0) {
                out.append(","); // 拼上逗号
            }

            // 序列化属性，如： "name":"tom"
            // 如果遇到属性没有值（null）,则返回空字符串：""
            String serializer = fieldSerializer.serializer(config, object);
            out.append(serializer);

            // 如果字段序列化后，是空字符串
            if (serializer.isEmpty()) {
                i = 0;
            } else {
                i = 1;
            }
        }
        // 说明最后一个字符是逗号，则需要手动把逗号删除
        if (out.lastIndexOf(",") == out.length() - 1) {
            out.deleteCharAt(out.length() - 1);
        }

        out.append("}");
    }
}
