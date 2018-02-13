package com.gank.android.app.utils;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.text.TextUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.gank.android.app.R;
import com.gank.android.app.ui.dialog.CommonDialog;
import com.scwang.smartrefresh.layout.internal.ProgressDrawable;

/**
 * 对话框辅助类
 * Created by 火蚁 on 15/6/19.
 */
public class DialogHelp {

    /***
     * 获取一个耗时等待对话框
     * @param context
     * @param message
     * @return
     */
    public static Dialog getProgressDialog(Context context, String message) {

        Dialog progressDialog = new Dialog(context, R.style.progress_dialog);
        if (TextUtils.isEmpty(message)) {
            progressDialog.setContentView(R.layout.dialog_progress_waiting);
        } else {
            progressDialog.setContentView(R.layout.dialog_progress_waiting_with_msg);
        }
        progressDialog.setCancelable(false);
        progressDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        TextView msg = (TextView) progressDialog.findViewById(R.id.id_tv_loadingmsg);
        ImageView mProgressView = (ImageView) progressDialog.findViewById(R.id.progress_wheel);
        final ProgressDrawable mProgressDrawable = new ProgressDrawable();
        mProgressDrawable.setColor(0xF8F8FF);
        mProgressView.setImageDrawable(mProgressDrawable);
        mProgressDrawable.start();
        msg.setText(message);
        progressDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                mProgressDrawable.stop();
            }
        });
        progressDialog.show();
        return progressDialog;
    }

    public static void showCommonDialog(Context context,
                                        String left,
                                        String right,
                                        String msg,
                                        DialogInterface.OnClickListener leftListener,
                                        DialogInterface.OnClickListener rightListener){



        CommonDialog.Builder builder = new CommonDialog.Builder(context);
        builder.setMessage(msg);
        if (leftListener != null) {
            builder.setLeftTextView(left,leftListener);
        }else {
            builder.setLeftTextView(left, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
        }
        if (rightListener != null) {
            builder.setRightTextView(right,rightListener);
        }else {
            builder.setRightTextView(right,
                    new android.content.DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
        }

        builder.create().show();
    }

}
