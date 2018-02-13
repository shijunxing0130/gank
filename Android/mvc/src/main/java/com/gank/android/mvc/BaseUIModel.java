package com.gank.android.mvc;

import java.io.Serializable;

/**
 * Author: Walid
 * Date: 2015/3/19 14:30
 */
public abstract class BaseUIModel implements Serializable {

	private boolean restored;

	public BaseUIModel() {}

	public boolean isRestored() {
		return restored;
	}

	public void setRestored(boolean restored) {
		this.restored = restored;
	}

}