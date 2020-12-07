package com.mei.myjson;

/**
 * @author mxb
 * @date 2020/12/6
 * @desc
 * @desired
 */


import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;


/**
 * @author Lance
 */
public class TypeReference<T> {

//    static ConcurrentMap<Type, Type> classTypeCache
//            = new ConcurrentHashMap<Type, Type>(16, 0.75f, 1);

    protected final Type type;


    protected TypeReference() {
        Type superClass = getClass().getGenericSuperclass();

        Type oriType = ((ParameterizedType) superClass).getActualTypeArguments()[0];
        type = oriType;
        //TODO FastJson中实现 没有测试到问题 如果有同学测试到可以说说
//        if (oriType instanceof Class) {
//            type = oriType;
//        } else {
//            //修复在安卓环境中问题
//            Type cachedType = classTypeCache.get(oriType);
//            if (cachedType == null) {
//                classTypeCache.putIfAbsent(oriType, oriType);
//                cachedType = classTypeCache.get(oriType);
//            }
//
//            type = cachedType;
//        }
    }


    public Type getType() {
        return type;
    }
}
