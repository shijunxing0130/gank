package com.gank.android.mvc;

import android.util.Log;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * Author: walid
 * Date ： 2016/3/28 17:52
 */
public class DynamicProxy implements InvocationHandler {

	private Object target;
	private Class<?> iClass;

	public DynamicProxy(Object target, Class<?> iClass) {
		this.target = target;
		this.iClass = iClass;
	}

	public Object getInterface() {
		return Proxy.newProxyInstance(iClass.getClassLoader(), new Class[]{iClass}, this);
	}

	@Override
	public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
		long start = System.currentTimeMillis();
		Object obj = method.invoke(target, args);
		Log.d(getClass().getName(), method.getName() + " cost time is:" + (System.currentTimeMillis() - start));
		return obj;
	}

}
