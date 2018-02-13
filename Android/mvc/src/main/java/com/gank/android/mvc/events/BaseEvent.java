package com.gank.android.mvc.events;

public interface BaseEvent {

    String getID();
    boolean isSuccess();
    String getMsg();
    int getStatus();


}
