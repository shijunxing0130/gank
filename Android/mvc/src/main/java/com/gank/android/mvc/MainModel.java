package com.gank.android.mvc;

/**
 * Author: walid
 * Date ： 2016/1/21 12:04
 */
public class MainModel extends BaseUIModel implements IMainModel {

	private String localName;

	private String globalName;

	public String getLocalName() {
		return localName;
	}

	public void setLocalName(String localName) {
		this.localName = localName;
	}

	public String getGlobalName() {
		return globalName;
	}

	public void setGlobalName(String globalName) {
		this.globalName = globalName;
	}
}