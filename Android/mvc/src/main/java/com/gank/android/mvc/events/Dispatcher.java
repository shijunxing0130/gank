package com.gank.android.mvc.events;

public interface Dispatcher {

	void addListener(String UIEventClassName, String type, BaseEventListener listener);

	void removeListener(String UIEventClassName, String type, BaseEventListener listener);

	boolean hasListener(String UIEventClassName, String type, BaseEventListener listener);

	void dispatchEvent(String UIEventClassName, BaseEvent event);

	void dispatchGlobalEvent(BaseEvent event);

	void dispose(String UIEventClassName);

	void disposeAllListener();

}