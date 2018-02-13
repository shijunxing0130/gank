package com.gank.android.mvc.events;

import java.io.Serializable;

/**
 * Author: walid
 * Date ： 2016/3/21 18:48
 */
public class EventDispatcherUtils {

	public static void addListener(String UIEventClassName, String type, BaseEventListener listener) {
		EventDispatcher.getInstance().addListener(UIEventClassName, type, listener);
	}

	public static <T extends Serializable> void addDataListener(String UIEventClassName, String type, DataEventListener<T> listener) {
		EventDispatcherUtils.addListener(UIEventClassName, type, listener);
	}

	public static void removeListener(String UIEventClassName, String type, NoticeEventListener listener) {
		EventDispatcher.getInstance().removeListener(UIEventClassName, type, listener);
	}

	public static boolean hasListener(String UIEventClassName, String type, NoticeEventListener listener) {
		return EventDispatcher.getInstance().hasListener(UIEventClassName, type, listener);
	}

	public static void dispatchEvent(String UIEventClassName, BaseEvent event) {
		EventDispatcher.getInstance().dispatchEvent(UIEventClassName, event);
	}

	public static <T extends Serializable> void dispatchDataEvent(String UIEventClassName, DataEvent<T> event) {
		EventDispatcherUtils.dispatchEvent(UIEventClassName, event);
	}

	public static void dispatchGlobalEvent(BaseEvent event) {
		EventDispatcher.getInstance().dispatchGlobalEvent(event);
	}

	public static <T extends Serializable> void dispatchGlobalDataEvent(DataEvent<T> event) {
		EventDispatcherUtils.dispatchGlobalEvent(event);
	}

	public static void dispose(String UIEventClassName) {
		EventDispatcher.getInstance().dispose(UIEventClassName);
	}

	public static void disposeAllListener() {
		EventDispatcher.getInstance().disposeAllListener();
	}

}
