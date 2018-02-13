package com.gank.android.mvc.events.entity;

import java.io.Serializable;

/**
 * Author: walid
 * Date ： 2016/3/29 13:06
 */
public class TopicCollectEntity implements Serializable {

	private int topicId;
	private boolean isCollect;

	public TopicCollectEntity(int topicId, boolean isCollect) {
		this.topicId = topicId;
		this.isCollect = isCollect;
	}

	public int getTopicId() {
		return topicId;
	}

	public void setTopicId(int topicId) {
		this.topicId = topicId;
	}

	public boolean isCollect() {
		return isCollect;
	}

	public void setIsCollect(boolean isCollect) {
		this.isCollect = isCollect;
	}
}
