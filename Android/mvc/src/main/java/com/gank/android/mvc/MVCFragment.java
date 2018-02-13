package com.gank.android.mvc;

import android.content.Context;

import com.gank.android.mvc.events.EventDispatcherUtils;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

import cn.gank.androidlibs.base.XFragment;

/**
 * Created by shijunxing on 2017/9/1.
 */

public abstract class MVCFragment<TC extends BaseController> extends XFragment {

    private String receiverIDCard = toString();
    public TC controller;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        initController();
    }

    private void initController() {
        Type genType = getClass().getGenericSuperclass();
        if (genType == null || !(genType instanceof ParameterizedType)) {
            return;
        }
        Type[] params = ((ParameterizedType) genType).getActualTypeArguments();
        String exceptionMessage = "";
        try {
            Constructor<TC> c = ((Class<TC>) params[0]).getConstructor(String.class);
            if (c != null) {
                controller = c.newInstance(receiverIDCard);
            }
        } catch (NoSuchMethodException e) {
            exceptionMessage = e.getMessage();
        } catch (InstantiationException e) {
            exceptionMessage = e.getMessage();
        } catch (IllegalAccessException e) {
            exceptionMessage = e.getMessage();
        } catch (InvocationTargetException e) {
            exceptionMessage = e.getMessage();
        } catch (java.lang.InstantiationException e) {
            e.printStackTrace();
        }

        if (controller == null) {
            throw new RuntimeException(exceptionMessage);
        }
    }


    public String getReceiverIDCard() {
        return receiverIDCard;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventDispatcherUtils.dispose(receiverIDCard);
    }
}
