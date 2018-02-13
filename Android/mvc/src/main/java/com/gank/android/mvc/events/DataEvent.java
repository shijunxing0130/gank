package com.gank.android.mvc.events;

/**
 * Author: walid
 * Date ： 2016/3/20 12:04
 */
public class DataEvent<T> implements BaseEvent {

	private T resultData;
	private String eventType;

	public static <T> DataEvent get(String eventType, T resultData) {
		return new DataEvent<>().setResultData(resultData).setEventType(eventType);
	}

	@Override
	public String getID() {
		return eventType;
	}

	@Override
	public boolean isSuccess() {
		return false;
	}

	@Override
	public String getMsg() {
		return null;
	}

	@Override
	public int getStatus() {
		return 0;
	}

	public DataEvent setEventType(String eventType) {
		this.eventType = eventType;
		return this;
	}

	public T getResultData() {
		return resultData;
	}

	public DataEvent setResultData(T resultData) {
		this.resultData = resultData;
		return this;
	}

}
