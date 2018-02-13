package com.gank.android.app.ui.dialog;

import android.view.View;

import com.gank.android.app.R;

/**
 * Created by shijunxing on 2017/10/11.
 */

public class GankHuoTypeDialog extends BaseBottomDialog {

    private OnDialogSelectListener mListener;

    public void setListener(OnDialogSelectListener listener) {
        mListener = listener;
    }

    @Override
    public int getLayoutRes() {
        return R.layout.layout_select_type_dialog;
    }

    @Override
    public void bindView(View v) {
        v.findViewById(R.id.tv_type_dialog_android).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mListener != null) {
                    mListener.onDialogSelect("Android");
                }
                dismiss();
            }
        });
        v.findViewById(R.id.tv_type_dialog_ios).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mListener != null) {
                    mListener.onDialogSelect("iOS");
                }
                dismiss();
            }
        });
        v.findViewById(R.id.tv_type_dialog_h5).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mListener != null) {
                    mListener.onDialogSelect("前端");
                }
                dismiss();
            }
        });
        v.findViewById(R.id.tv_type_dialog_app).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mListener != null) {
                    mListener.onDialogSelect("App");
                }
                dismiss();
            }
        });

    }

    @Override
    public float getDimAmount() {
        return 0.3f;
    }

    public interface OnDialogSelectListener {
        void onDialogSelect(String type);
    }
}
