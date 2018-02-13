package com.gank.android.app.even;

import cn.gank.androidlibs.event.IBus;

/**
 * Created by shijunxing on 2017/9/16.
 */

public class BaseEven implements IBus.IEvent {

    public String data;
    public int type;

    public BaseEven(int type) {
        this.type = type;
    }

    public BaseEven(String data, int type) {
        this.data = data;
        this.type = type;
    }

    @Override
    public int getTag() {
        return type;
    }
}
