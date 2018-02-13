package com.gank.android.mvc;

import android.os.Bundle;
import android.view.View;

/**
 * Author: walid
 * Date ： 2016/1/21 12:04
 */
public class MainActivity extends MVCActivity<MainController> {

	@Override
	public void initVariables() {

	}

	@Override
	public void initViews(View view, Bundle savedInstanceState) {

	}

	@Override
	public void loadData() {

	}

	@Override
	public int getLayoutId() {
		return 0;
	}
//
//	@Override
//	protected void initView() {
//		setContentView(R.layout.activity_main);
//		ButterKnife.bind(this);
//	}
//
//	@Override
//	protected void initData() {
//		// 注意要先addListener 否则监听是无效的
//		String receiverIDCard = getReceiverIDCard();
//		EventDispatcherUtils.addListener(receiverIDCard, LocalEvent.LOCAL_EVENT, new NoticeEventListener() {
//			@Override
//			public void onEvent(BaseEvent event) {
//				Toast.makeText(getApplicationContext(), controller.getIModel().getLocalName(), Toast.LENGTH_SHORT).show();
//			}
//		});
//		EventDispatcherUtils.addDataListener(receiverIDCard, Global.GLOBAL_EVENT, new DataEventListener<Boolean>() {
//			@Override
//			public void onEvent(DataEvent<Boolean> event) {
//				boolean datas = event.getResultData();
//				Toast.makeText(getApplicationContext(), controller.getIModel().getGlobalName(), Toast.LENGTH_SHORT).show();
//			}
//		});
//
//		controller.requestLocalDatas();
//		controller.requestGlobalDatas();
//	}

}