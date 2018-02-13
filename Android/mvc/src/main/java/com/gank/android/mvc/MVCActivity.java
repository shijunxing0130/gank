package com.gank.android.mvc;

import android.os.Bundle;
import com.gank.android.mvc.events.EventDispatcherUtils;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

import cn.gank.androidlibs.base.XActivity;

/**
 * Author: walid
 * Date ： 2016/1/7 18:00
 */
public abstract class MVCActivity<TC extends BaseController> extends XActivity {

	private String receiverIDCard = toString();
	public TC controller;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		initController();
		super.onCreate(savedInstanceState);
	}

	private void initController() {
		Type genType = getClass().getGenericSuperclass();
		if (genType == null || !(genType instanceof ParameterizedType)) {
			return;
		}
		Type[] params = ((ParameterizedType) genType).getActualTypeArguments();
		String exceptionMessage = "";
		try {
			Constructor<TC> c = ((Class<TC>) params[0]).getConstructor(String.class);
			if (c != null) {
				controller = c.newInstance(receiverIDCard);
			}
		} catch (NoSuchMethodException e) {
			exceptionMessage = e.getMessage();
		} catch (InstantiationException e) {
			exceptionMessage = e.getMessage();
		} catch (IllegalAccessException e) {
			exceptionMessage = e.getMessage();
		} catch (InvocationTargetException e) {
			exceptionMessage = e.getMessage();
		}

		if (controller == null) {
			throw new RuntimeException(exceptionMessage);
		}
	}


	public String getReceiverIDCard() {
		return receiverIDCard;
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		EventDispatcherUtils.dispose(receiverIDCard);
	}

}