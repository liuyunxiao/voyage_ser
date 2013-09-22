package com.common.util;

import java.lang.reflect.Method;

public class ReflectUtil {
	/**
	 * 普通方法的反射调用
	 * @param target
	 * @param methodName
	 * @param args
	 * @param isArgInterface 参数是不是借口类型
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public static Object invokeMethod(Object target, String methodName, Object[] args, boolean isArgInterface) throws Exception {
		Class c = target.getClass();
		Class[] argsClass = null;
		if (args != null) {
			argsClass = new Class[args.length];
			for (int i = 0, j = args.length; i < j; i++) {
				if (!isArgInterface)
					argsClass[i] = args[i].getClass();
				else
					argsClass[i] = args[i].getClass().getInterfaces()[0];
			}
		}
		Method method = null;
		try {
			method = c.getDeclaredMethod(methodName, argsClass);
		} catch (NoSuchMethodException e) {
			method = c.getSuperclass().getDeclaredMethod(methodName, argsClass);//父类中尝试一次
		}

		method.setAccessible(true);
		return method.invoke(target, args);
	}

	@SuppressWarnings("unchecked")
	public static Object invokeStaticMethod(String className, String methodName, Object[] args) throws Exception {
		Class c = Class.forName(className);
		Class[] argsClass = null;
		if (args != null) {
			argsClass = new Class[args.length];
			for (int i = 0, j = args.length; i < j; i++) {
				argsClass[i] = args[i].getClass();
			}
		}
		Method method = c.getDeclaredMethod(methodName, argsClass);
		method.setAccessible(true);
		return method.invoke(null, args);
	}
}
