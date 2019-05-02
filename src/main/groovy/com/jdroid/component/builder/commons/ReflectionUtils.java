package com.jdroid.component.builder.commons;

import java.lang.reflect.Method;

/**
 * Reflection related utilities
 */
public abstract class ReflectionUtils {

	public static Object invokeMethod(Object obj, String methodName) {
		try {
			Method method = obj.getClass().getMethod(methodName);
			return method.invoke(obj);
		} catch (Exception e) {
			return null;
		}
	}
}
