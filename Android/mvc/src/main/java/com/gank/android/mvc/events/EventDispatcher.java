package com.gank.android.mvc.events;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Author: Wesley
 * Date ： 2016/3/19 18:00
 */
public final class EventDispatcher implements Dispatcher {

	// 第一个string【Key】代表class名字   第二个String【Key】代表eventType
	private final ConcurrentHashMap<String, Map<String, CopyOnWriteArrayList<BaseEventListener>>> classListenerMap =
			new ConcurrentHashMap<>();

	static class SingletonHolder {
		static EventDispatcher instance = new EventDispatcher();
	}

	public static EventDispatcher getInstance() {
		return SingletonHolder.instance;
	}

	private EventDispatcher() {

	}

	@Override
	public void addListener(String receiverIDCard, String type, BaseEventListener listener) {
		synchronized (classListenerMap) {
			Map<String, CopyOnWriteArrayList<BaseEventListener>> listenerMap = classListenerMap.get(receiverIDCard);
			if (listenerMap == null) {
				listenerMap = new HashMap<>();
				classListenerMap.put(receiverIDCard, listenerMap);
			}
			CopyOnWriteArrayList<BaseEventListener> list = listenerMap.get(type);
			if (list == null) {
				list = new CopyOnWriteArrayList<>();
				listenerMap.put(type, list);
			}
			list.add(listener);
		}
	}

	@Override
	public void removeListener(String receiverIDCard, String type, BaseEventListener listener) {
		synchronized (classListenerMap) {
			if (hasListener(receiverIDCard, type, listener)) {
				Map<String, CopyOnWriteArrayList<BaseEventListener>> listenerMap = classListenerMap.get(receiverIDCard);
				if (listenerMap == null) {
					return;
				}
				CopyOnWriteArrayList<BaseEventListener> list = listenerMap.get(type);
				if (list == null) {
					return;
				}
				list.remove(listener);
			}
		}
	}

	@Override
	public boolean hasListener(String receiverIDCard, String type, BaseEventListener listener) {
		synchronized (classListenerMap) {
			Map<String, CopyOnWriteArrayList<BaseEventListener>> listenerMap = classListenerMap.get(receiverIDCard);
			if (listenerMap == null) {
				return false;
			}
			CopyOnWriteArrayList<BaseEventListener> list = listenerMap.get(type);
			return list != null && list.contains(listener);
		}
	}

	@Override
	public void dispatchEvent(String receiverIDCard, BaseEvent event) {
		if (event == null) {
			return;
		}
		synchronized (classListenerMap) {
			String type = event.getID();
			CopyOnWriteArrayList<BaseEventListener> list;
			Map<String, CopyOnWriteArrayList<BaseEventListener>> listenerMap = classListenerMap.get(receiverIDCard);
			if (listenerMap == null) {
				return;
			}
			list = listenerMap.get(type);
			if (list == null) {
				return;
			}
			for (BaseEventListener listener : list) {
				if (listener != null) {
					if (listener instanceof DataEventListener && event instanceof DataEvent) {
						((DataEventListener) listener).onEvent((DataEvent) event);
					} else if (listener instanceof NoticeEventListener) {
						((NoticeEventListener) listener).onEvent(event);
					}
				}
			}
		}
	}

	@Override
	public void dispatchGlobalEvent(BaseEvent event) {
		if (event == null) {
			return;
		}
		synchronized (classListenerMap) {
			for (String key : classListenerMap.keySet()) {
				String type = event.getID();
				CopyOnWriteArrayList<BaseEventListener> list;
				Map<String, CopyOnWriteArrayList<BaseEventListener>> listenerMap = classListenerMap.get(key);
				if (listenerMap == null) {
					continue;
				}
				list = listenerMap.get(type);
				if (list == null) {
					continue;
				}
				for (BaseEventListener listener : list) {
					if (listener != null) {
						if (listener instanceof DataEventListener && event instanceof DataEvent) {
							((DataEventListener) listener).onEvent((DataEvent) event);
						} else if (listener instanceof NoticeEventListener) {
							((NoticeEventListener) listener).onEvent(event);
						}
					}
				}
			}
		}
	}

	@Override
	public void dispose(String receiverIDCard) {
		synchronized (classListenerMap) {
			Map<String, CopyOnWriteArrayList<BaseEventListener>> listenerMap = classListenerMap.get(receiverIDCard);
			if (listenerMap == null) {
				return;
			}
			listenerMap.clear();
			classListenerMap.remove(receiverIDCard);
		}
	}

	@Override
	public void disposeAllListener() {
		synchronized (classListenerMap) {
			classListenerMap.clear();
		}
	}

}
