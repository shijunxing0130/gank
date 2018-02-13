package com.gank.android.mvc;

import android.os.Bundle;
import com.gank.android.mvc.events.BaseEvent;
import com.gank.android.mvc.events.DataEvent;
import com.gank.android.mvc.events.EventDispatcher;
import com.gank.android.mvc.events.EventDispatcherUtils;

import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;


/**
 * Author: Walid
 * Date: 2015/3/19 14:30
 */
public abstract class BaseController<TM extends BaseUIModel, TIM> implements Serializable {

	public String receiverIDCard;
	private TM model;

	public BaseController(String receiverIDCard) {
		this.receiverIDCard = receiverIDCard;
		Type genType = getClass().getGenericSuperclass();
		if (genType == null || !(genType instanceof ParameterizedType)) {
			return;
		}
		Type[] params = ((ParameterizedType) genType).getActualTypeArguments();
		String exceptionMessage = "";
		try {
			model = ((Class<TM>) params[0]).newInstance();
		} catch (InstantiationException e) {
			exceptionMessage = e.getMessage();
		} catch (IllegalAccessException e) {
			exceptionMessage = e.getMessage();
		}

		if (model == null) {
			throw new RuntimeException(exceptionMessage);
		}
	}

	protected TM getModel() {
		return model;
	}



	protected final void dispatchEvent(BaseEvent event) {
		EventDispatcher.getInstance().dispatchEvent(receiverIDCard, event);
	}

	protected final <T extends Serializable> void dispatchDataEvent(DataEvent<T> event) {
		EventDispatcherUtils.dispatchDataEvent(receiverIDCard, event);
	}

	protected void dispatchGlobalEvent(BaseEvent event) {
		EventDispatcher.getInstance().dispatchGlobalEvent(event);
	}

	public boolean isRestored() {
		return model.isRestored();
	}

	private TIM iModel;

	public TIM getIModel() {
		if (iModel == null) {
			Type genType = getClass().getGenericSuperclass();
			if (genType == null || !(genType instanceof ParameterizedType)) {
				throw new IllegalStateException("");
			}
			Type[] params = ((ParameterizedType) genType).getActualTypeArguments();
			DynamicProxy dynamicProxy = new DynamicProxy(model, (Class<?>) params[1]);
			iModel = (TIM) dynamicProxy.getInterface();
		}
		return iModel;
	}

	public void onSaveInstanceState(Bundle outState) {
		outState.putSerializable("model", model);
	}

	public void onRestoreInstanceState(Bundle savedInstanceState) {
		if (savedInstanceState != null) {
			Object object = savedInstanceState.getSerializable("model");
			if (object != null) {
				model = (TM) object;
				model.setRestored(true);
			}
		}
	}

}