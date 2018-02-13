package com.gank.android.mvc.events;

import cn.gank.androidlibs.httphelper.BaseResponse;

public class NoticeEvent implements BaseEvent {

	private String id;
	private boolean success;
	private int status;
	private String msg;

	public NoticeEvent(String id) {
		this.id = id;
	}

	public NoticeEvent(String id, int status, String msg) {
		this.id = id;
		this.success = status == 0;
		this.status = status;
		this.msg = msg;
	}

	public NoticeEvent(String id,BaseResponse baseResponse) {
		this.id = id;
		this.status = baseResponse.getStatus();
		this.msg = baseResponse.getMsg();
		this.success = baseResponse.getStatus() == 0;
	}

	@Override
	public String getID() { return id; }

	@Override
	public boolean isSuccess() {
		return success;
	}

	@Override
	public String getMsg() {
		return msg;
	}

	@Override
	public int getStatus() {
		return status;
	}

	public void setType(String type) {
		this.id = type;
	}


}
