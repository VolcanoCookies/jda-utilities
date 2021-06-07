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
	
	public static Type stripWildcard(Type type) {
		try {
			return ((ParameterizedType) type).getRawType();
		} catch (Exception e) {
			return type;
		}
	}
	
	public static Class<?> dePrimitivize(Class<?> clazz) {
		if (clazz.isPrimitive()) {
			if (clazz == Byte.TYPE) {
				return Byte.class;
			} else if (clazz == Short.TYPE) {
				return Short.class;
			} else if (clazz == Integer.TYPE) {
				return Integer.class;
			} else if (clazz == Long.TYPE) {
				return Long.class;
			} else if (clazz == Boolean.TYPE) {
				return Boolean.class;
			} else if (clazz == Character.TYPE) {
				return Character.class;
			} else if (clazz == Float.TYPE) {
				return Float.class;
			} else if (clazz == Double.TYPE) {
				return Double.class;
			}
		}
		return clazz;
	}
	
}
