package com.gank.android.app.ui.dialog;

import android.view.View;

import com.gank.android.app.R;
import com.gank.android.app.controller.UserController;
import com.gank.android.app.model.UserModelImpl;

/**
 * Created by shijunxing on 2017/10/11.
 */

public class SelectImageDialog extends BaseBottomDialog {

    private OnDialogSelectListener mListener;

    public void setListener(OnDialogSelectListener listener) {
        mListener = listener;
    }

    @Override
    public int getLayoutRes() {
        return R.layout.layout_select_image_dialog;
    }

    @Override
    public void bindView(View v) {
     v.findViewById(R.id.tv_select_dialog_camera).setOnClickListener(new View.OnClickListener() {
         @Override
         public void onClick(View v) {
             if (mListener != null) {
                 mListener.onDialogSelect(UserController.REQUEST_CODE_CAMERA);
             }
             dismiss();
         }
     });
     v.findViewById(R.id.tv_select_dialog_gallery).setOnClickListener(new View.OnClickListener() {
         @Override
         public void onClick(View v) {
             if (mListener != null) {
                 mListener.onDialogSelect(UserController.REQUEST_CODE_GALLERY);
             }
             dismiss();
         }
     });
     v.findViewById(R.id.tv_select_dialog_cancle).setOnClickListener(new View.OnClickListener() {
         @Override
         public void onClick(View v) {
             dismiss();
         }
     });

    }

    @Override
    public float getDimAmount() {
        return 0.3f;
    }

    public interface OnDialogSelectListener{
        void onDialogSelect(int type);
    }
}
