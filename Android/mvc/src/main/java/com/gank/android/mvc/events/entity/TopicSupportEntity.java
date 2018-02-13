package com.gank.android.mvc.events.entity;

import java.io.Serializable;

/**
 * Author: walid
 * Date ： 2016/3/29 13:06
 */
public class TopicSupportEntity implements Serializable {

	private int topicId;
	private boolean isSupport;

	public TopicSupportEntity(int topicId, boolean isSupport) {
		this.topicId = topicId;
		this.isSupport = isSupport;
	}

	public int getTopicId() {
		return topicId;
	}

	public void setTopicId(int topicId) {
		this.topicId = topicId;
	}

	public boolean isSupport() {
		return isSupport;
	}

	public void setIsSupport(boolean isSupport) {
		this.isSupport = isSupport;
	}
}
