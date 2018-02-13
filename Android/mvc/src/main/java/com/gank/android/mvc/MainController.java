package com.gank.android.mvc;

import com.gank.android.mvc.events.DataEvent;
import com.gank.android.mvc.events.NoticeEvent;
import com.gank.android.mvc.events.type.Global;
import com.gank.android.mvc.events.type.LocalEvent;

/**
 * Author: walid
 * Date ： 2016/1/21 12:04
 */
public class MainController extends BaseController<MainModel, IMainModel> {

	public MainController(String UIEventClassName) {
		super(UIEventClassName);
	}

	public void requestLocalDatas() {
		getModel().setLocalName("localName");
		dispatchEvent(new NoticeEvent(LocalEvent.LOCAL_EVENT));
	}

	public void requestGlobalDatas() {
		getModel().setGlobalName("globalName");
		dispatchGlobalEvent(DataEvent.get(Global.GLOBAL_EVENT, true));
	}

}
