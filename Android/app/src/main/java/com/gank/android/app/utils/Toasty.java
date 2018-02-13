package com.gank.android.app.utils;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.gank.android.app.R;


public class Toasty {
    private Toast toast;
    private View layout;
    private static Toasty instance;

    private Toasty() {
    }

    public static Toasty getInstance() {
        if (instance == null) {
            synchronized (Toasty.class) {
                if (instance == null) {
                    instance = new Toasty();
                }
            }
        }
        return instance;
    }


    public void showToast(Context context, String tvString) {
        showToast(context, null, tvString);
    }



    private void showToast(Context context, ViewGroup root, String tvString) {
        if (layout == null) {
            layout = LayoutInflater.from(context).inflate(R.layout.layout_toast_gank, root);
        }
        TextView text = (TextView) layout.findViewById(R.id.text);
        text.setText(tvString);
        if (toast == null) {
            toast = new Toast(context);
            toast.setGravity(Gravity.CENTER, 0, 0);
            toast.setDuration(Toast.LENGTH_SHORT);
            toast.setView(layout);
        } else {
            toast.setView(layout);
            toast.setDuration(Toast.LENGTH_SHORT);
        }

        toast.show();

    }


}
