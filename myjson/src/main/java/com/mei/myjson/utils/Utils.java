package com.mei.myjson.utils;

import com.mei.myjson.FieldInfo;
import com.mei.myjson.serializer.FieldSerializer;


import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.WildcardType;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * @author mxb
 * @date 2020/12/5
 * @desc
 * @desired
 */
public class Utils {

    private static final String TAG = "Utils";

    /**
     * 是否是基础数据类型的包装类型
     *
     * @param type 数据类型
     * @return true：是基础类型的包装类型，false：不是基础类型的包装类型
     */
    public static boolean isBox(Class type) {
        return type == Integer.class
                || type == Double.class
                || type == Float.class
                || type == Long.class
                || type == Short.class
                || type == Character.class
                || type == Byte.class
                || type == Boolean.class;
    }

    /**
     * 是否是字符串类型
     *
     * @param type 数据类类型
     * @return true：是字符串类型，false：不是字符串类型
     */
    public static boolean isString(Class type) {
        // 判断type是不是CharSequence或者其子类类型
        return CharSequence.class.isAssignableFrom(type);
    }

    /**
     * 获取指定类本身和其父类所有的成员属性
     *
     * @param clazz JavaBean类型
     */
    public static Map<String, Field> parseAllFieldToCache(Class<?> clazz) {
        Map<String, Field> fieldMap = new HashMap<>();
        parseAllFieldToCache(fieldMap, clazz);
        return fieldMap;
    }

    /**
     * 获取指定类本身和其父类所有的成员属性
     *
     * @param fieldMap 用于存放查找到的成员属性
     * @param clazz    JavaBean类型
     */
    public static void parseAllFieldToCache(Map<String, Field> fieldMap,
            Class<?> clazz) {
        if (fieldMap == null || clazz == null) {
            throw new NullPointerException("fieldMap is null or clazz is null");
        }

        // 1. 获取该类自己定义的所有属性
        Field[] fields = clazz.getDeclaredFields();
        for (Field field : fields) {
            String name = field.getName();
            Log.i(TAG, "parseAllFieldToCache: fieldName=" + name);
            // 避免重复添加
            if (!fieldMap.containsKey(name)) {
                fieldMap.put(name, field);
            }
        }

        // 2. 查找父类定义的所有属性
        if (clazz.getSuperclass() != null && clazz.getSuperclass() != Object.class) {
            // 如果父类是Object，则忽略
            parseAllFieldToCache(fieldMap, clazz.getSuperclass());
        }
    }

    /**
     * 获取可以被序列化的成员属性 所对应的属性序列化器集合
     * 可以被序列化的属性包括：当前类和父类get函数，is函数，和public成员属性
     *
     * @param clazz    JavaBean类型
     * @param fieldMap JavaBean中所有的属性集合
     * @return JavaBean中可以被序列化的属性 所对应的属性序列化器集合
     */
    public static List<FieldSerializer> computeGetters(Class<?> clazz,
            Map<String, Field> fieldMap) {
        Map<String, FieldInfo> fieldInfoMap = new LinkedHashMap<>();

        // 1. 获取当前类所有的公有函数，并筛选符合条件的方法存入到fieldInfoMap集合中
        Method[] methods = clazz.getMethods();// 获取的全都是公有方法
        for (Method method : methods) {
            // 获取方法名
            String methodName = method.getName();
            Log.i(TAG, "computeGetters: methodName=" + methodName);
            // 排除static类型的方法
            if (Modifier.isStatic(method.getModifiers())) {
                continue;
            }

            // 排除返回值是void的方法
            if (method.getReturnType().equals(Void.TYPE)) {
                continue;
            }

            // 排除有参数的方法
            if (method.getParameterTypes().length != 0) {
                continue;
            }

            // 排除getClass方法
            if ("getClass".equals(methodName)) {
                continue;
            }

            // 属性名
            String propertyName;
            // 1. 以get开头的方法，即getter方法，如：getAge()
            if (methodName.startsWith("get")) {
                // 必须4个或者4个字符以上的函数名
                if (methodName.length() < 4) {
                    continue;
                }
                // get后的第一个字母
                char c3 = methodName.charAt(3);
                // 把get后的第一个字符变成小写，在加上剩余的字符，就是字段名：a+ge,即age。
                propertyName = Character.toLowerCase(c3) + methodName.substring(4);
                Log.i(TAG, "computeGetters: propertyName=" + propertyName);

                // 根据字段名从JavaBean的字段集合中获取对应的字段对象，可能为null
                Field field = fieldMap.get(propertyName);
                // 把JavaBean的字段名，字段对象和字段对应的get方法，保存到一个FiledInfo对象中
                FieldInfo fieldInfo = new FieldInfo(propertyName, field, method);
                fieldInfoMap.put(propertyName, fieldInfo);
            }

            // 2. 获取is开头的方法
            if (methodName.startsWith("is")) {
                // is开头的方法，必须大于3
                if (methodName.length() < 3) {
                    continue;
                }

                // is后的第一个字符
                char c2 = methodName.charAt(2);
                // 由方法名，拼接属性名
                propertyName = Character.toLowerCase(c2) + methodName.substring(3);
                Log.i(TAG, "computeGetters: propertyName=" + propertyName);
                // 获取JavaBean的字段对象
                Field field = fieldMap.get(propertyName);
                FieldInfo fieldInfo = new FieldInfo(propertyName, field, method);
                fieldInfoMap.put(propertyName, fieldInfo);
            }
        }

        // 2. 所有的公共字段
        Field[] fields = clazz.getFields();
        for (Field field : fields) {
            // 排除静态字段
            if (Modifier.isStatic(field.getModifiers())) {
                continue;
            }
            String propertyName = field.getName();
            Log.i(TAG, "computeGetters 公共属性: propertyName=" + propertyName);
            // 避免重复添加
            if (!fieldInfoMap.containsKey(propertyName)) {
                // 公有方法，即使没有getter方法，也能获取到属性值，
                FieldInfo fieldInfo = new FieldInfo(propertyName, field, null);
                fieldInfoMap.put(propertyName, fieldInfo);
            }
        }

        // 3. 把所有符合要求的字段，都用一个字段序列化器包装，并存入集合中
        List<FieldSerializer> fieldSerializers = new ArrayList<>();
        for (FieldInfo fieldInfo : fieldInfoMap.values()) {
            fieldSerializers.add(new FieldSerializer(fieldInfo));
        }

        return fieldSerializers;
    }

    /**
     * 采集可以被反序列化的成员属性
     * 可以被反序列化的属性需满足：当前类和父类set函数，和public成员属性*
     *
     * 即：反序列化 采集公有set函数与公有属性
     *
     * @param beanType 将要序列化成的JavaBean类型
     * @param fieldMap 当前JavaBean对象，所有的成员属性集合
     * @return 采集到的当前JavaBean类可以被赋值的所有成员属性集合
     */
    public static List<FieldInfo> computeSetter(Class<?> beanType, Map<String, Field> fieldMap) {
        Map<String, FieldInfo> fieldInfoMap = new HashMap<>();
        // 1. 获取当前类以及其父类的所有公有函数
        Method[] methods = beanType.getMethods();
        for (Method method : methods) {
            // 排除静态方法
            if (Modifier.isStatic(method.getModifiers())) {
                continue;
            }
            // 排除有返回值的方法
            if (method.getReturnType() != Void.TYPE) {
                continue;
            }
            // 排除不是只有一个参数的方法
            if (method.getParameterTypes().length != 1) {
                continue;
            }

            String propertyName;
            String methodName = method.getName();
            Log.i(TAG, "methodName=" + methodName);
            // 采集setter方法
            if (methodName.startsWith("set")) {
                // 排除void set()方法
                if (methodName.length() < 4) {
                    continue;
                }

                // 拼接字段名
                char c3 = methodName.charAt(3);
                propertyName = Character.toLowerCase(c3) + methodName.substring(4);
                Log.i(TAG, "propertyName=" + propertyName);
                // 避免字段重复天乩
                if (!fieldInfoMap.containsKey(propertyName)) {
                    // 根据字段名，获取字段对象
                    Field field = fieldMap.get(propertyName);
                    FieldInfo fieldInfo = new FieldInfo(propertyName, field, method, true);
                    fieldInfoMap.put(propertyName, fieldInfo);
                }
            }
        }

        // 2. 采集所有的公有字段
        Field[] fields = beanType.getFields();
        for (Field field : fields) {
            int modifiers = field.getModifiers();
            // 排除静态属性和final属性
            if (Modifier.isStatic(modifiers) || Modifier.isFinal(modifiers)) {
                continue;
            }
            String propertyName = field.getName();
            Log.i(TAG, "propertyName=" + propertyName);
            if (!fieldInfoMap.containsKey(propertyName)) {
                FieldInfo fieldInfo = new FieldInfo(propertyName, field, null, true);
                fieldInfoMap.put(propertyName, fieldInfo);
            }
        }

        List<FieldInfo> list = new ArrayList<>();
        list.addAll(fieldInfoMap.values());
        return list;
    }

    /**
     * 根据Type类型，获取真实的参数类型
     *
     * @param fieldType 字段类型
     */
    public static Type getItemType(Type fieldType) {
        if (fieldType instanceof Class) {
            return fieldType;
        }

        if (fieldType instanceof ParameterizedType) {
            Type actualTypeArgument = ((ParameterizedType) fieldType).getActualTypeArguments()[0];
            if (actualTypeArgument instanceof WildcardType) {
                WildcardType wildcardType = (WildcardType) actualTypeArgument;
                Type[] upperBounds = wildcardType.getUpperBounds();
                if (upperBounds.length == 1) {
                    actualTypeArgument = upperBounds[0];
                }
            }
            return actualTypeArgument;
        }
        return Object.class;
    }
}
