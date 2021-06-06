package net.volcano.jdautils.utils;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

public class ClassUtil {
	
	public static Type getGenericType(Object obj) {
		return ((ParameterizedType) obj.getClass().getGenericSuperclass()).getActualTypeArguments()[0];
	}
	
}
