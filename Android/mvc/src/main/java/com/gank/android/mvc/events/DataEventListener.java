package com.gank.android.mvc.events;

import java.io.Serializable;

/**
 * Author: walid
 * Date ： 2016/3/20 12:04
 */
public interface DataEventListener<T extends Serializable> extends BaseEventListener {

	void onEvent(DataEvent<T> event);

}
