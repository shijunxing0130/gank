package com.gank.android.app.ui.dialog;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.gank.android.app.R;


public class CommonDialog extends Dialog {

    public CommonDialog(Context context) {
        super(context);
    }

    public CommonDialog(Context context, int themeResId) {
        super(context, themeResId);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Window window = getWindow();
        assert window != null;
        window.setWindowAnimations(R.style.dialogWindowAnim);
    }


    public static class Builder {
        private Context context;
        private String message;
        private String LeftTextView;
        private String RightTextView;
        private int RightTextViewColorResID = 0;
        private View contentView;
        private DialogInterface.OnClickListener leftTextViewClickListener;
        private DialogInterface.OnClickListener rightTextViewClickListener;

        public Builder(Context context) {
            this.context = context;
        }

        public Builder setMessage(String message) {
            this.message = message;
            return this;
        }

        public Builder setContentView(View v) {
            this.contentView = v;
            return this;
        }


        public Builder setRightTextView(String rightTextView, DialogInterface.OnClickListener listener) {

            this.RightTextView = rightTextView;
            this.rightTextViewClickListener = listener;
            return this;
        }

        public Builder setRightTextViewColor(int resId) {
            this.RightTextViewColorResID = resId;
            return this;
        }


        public Builder setLeftTextView(String leftTextView, DialogInterface.OnClickListener listener) {

            this.LeftTextView = leftTextView;
            this.leftTextViewClickListener = listener;
            return this;
        }

        public CommonDialog create() {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            final CommonDialog dialog = new CommonDialog(context, R.style.Dialog);
            View layout = inflater.inflate(R.layout.dialog_common_layout, null);
            dialog.addContentView(layout, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));

            View view = layout.findViewById(R.id.middle_view);
            if (LeftTextView != null) {
                ((TextView) layout.findViewById(R.id.tv_simple_dialog_left)).setText(LeftTextView);
                if (leftTextViewClickListener != null) {
                    layout.findViewById(R.id.tv_simple_dialog_left).setOnClickListener(new View.OnClickListener() {
                        public void onClick(View v) {
                            leftTextViewClickListener.onClick(dialog, DialogInterface.BUTTON_NEGATIVE);
                        }
                    });

                }
            } else {
                layout.findViewById(R.id.tv_simple_dialog_left).setVisibility(View.GONE);
                view.setVisibility(View.GONE);
            }
            if (RightTextView != null) {
                TextView rightTextView = ((TextView) layout.findViewById(R.id.tv_simple_dialog_right));
                rightTextView.setText(RightTextView);
                if (RightTextViewColorResID != 0) {
                    rightTextView.setTextColor(context.getResources().getColor(RightTextViewColorResID));
                }
                if (rightTextViewClickListener != null) {
                    rightTextView.setOnClickListener(new View.OnClickListener() {
                        public void onClick(View v) {
                            rightTextViewClickListener.onClick(dialog, DialogInterface.BUTTON_POSITIVE);
                        }
                    });

                }
            } else {
                layout.findViewById(R.id.tv_simple_dialog_right).setVisibility(View.GONE);
                view.setVisibility(View.GONE);
            }
            if (message != null) {
                ((TextView) layout.findViewById(R.id.message)).setText(message);
            } else if (contentView != null) {
                ((LinearLayout) layout.findViewById(R.id.content)).removeAllViews();
                ((LinearLayout) layout.findViewById(R.id.content))
                        .addView(contentView, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.FILL_PARENT));
            }
            dialog.setContentView(layout);
            dialog.setCanceledOnTouchOutside(false);
            return dialog;
        }
    }

}
