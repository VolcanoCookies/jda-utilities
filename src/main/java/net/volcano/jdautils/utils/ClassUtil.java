package net.volcano.jdautils.utils;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

public class ClassUtil {
	
	public static Type getGenericType(Class<?> clazz) {
		return ((ParameterizedType) clazz.getGenericSuperclass()).getActualTypeArguments()[0];
	}
	
	public static Class<?> stripWildcard(Class<?> clazz) {
		try {
			return (Class<?>) ((ParameterizedType) clazz.getGenericSuperclass()).getRawType();
		} catch (Exception e) {
			return clazz;
		}
	}
	
}
