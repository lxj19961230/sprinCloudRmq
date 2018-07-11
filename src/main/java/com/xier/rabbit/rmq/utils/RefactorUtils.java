package com.xier.rabbit.rmq.utils;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

 public class RefactorUtils {
    public static Object newInstance(Class target,Object...parameters){
        try {
            if(parameters == null || parameters.length == 0) {
                return target.newInstance();
            }
            Class[] parameterTypes = new Class[parameters.length];
            for(int i = 0 ; i < parameters.length ; i++) {
                parameterTypes[i] = convertPrimitive(parameters[i].getClass());
            }
            Constructor constructor = target.getConstructor(parameterTypes);
            return constructor.newInstance(parameters);
        } catch (NoSuchMethodException e) {
           //TODO 需要特殊处理
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }

    private static Class convertPrimitive(Class clazz) {
        if(clazz == Boolean.class) return boolean.class;
        if(clazz == Character.class) return char.class;
        if(clazz == Byte.class) return byte.class;
        if(clazz == Short.class) return short.class;
        if(clazz == Integer.class) return int.class;
        if(clazz == Long.class) return long.class;
        if(clazz == Float.class) return float.class;
        if(clazz == Double.class) return double.class;
        return clazz;
    }
}
