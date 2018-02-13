package com.gank.android.app.ui.dialog;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.gank.android.app.R;

import com.gank.android.app.aouth.User;
import com.gank.android.app.ui.adapter.BaseViewHolder;
import com.zhy.autolayout.utils.AutoUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


/**
 * @author shaohui
 * @date 16/10/12
 */

public class MoreDialog extends BaseBottomDialog {

    public static final int SHARE_TYPE_WECHAT = 1;
    public static final int SHARE_TYPE_WECHAT_CIRCLE = 2;
    public static final int SHARE_TYPE_QQ = 3;
    public static final int SHARE_TYPE_QZONE = 4;
    public static final int SHARE_TYPE_WEIBO = 5;
    public static final int SHARE_TYPE_FRESH = 6;
    public static final int SHARE_TYPE_COPY_LINK = 7;
    public static final int SHARE_TYPE_COLLECT = 8;
    public static final int SHARE_TYPE_SHARE = 9;
    public static final int SHARE_TYPE_BROWSER = 10;

    private OnDialogSelectListener mListener;


    private String shareUrl;
    private String shareText;

    private RecyclerView rvSystem;
    private boolean isCollect;
    private boolean isShowCollect;

    public void setShareInfo(String shareUrl, String shareText) {
        this.shareUrl = shareUrl;
        this.shareText = shareText;
    }

    public void setListener(OnDialogSelectListener listener) {
        mListener = listener;
    }

    public boolean isCollect() {
        return isCollect;
    }

    public void setCollect(boolean isCollect) {
        this.isCollect = isCollect;
        if (rvSystem != null) {
            rvSystem.getAdapter().notifyDataSetChanged();
        }

    }

    public static MoreDialog getInstance(boolean isShowCollect) {
        MoreDialog moreDialog = new MoreDialog();
        moreDialog.isShowCollect = isShowCollect;
        return moreDialog;
    }

    @Override
    public int getLayoutRes() {
        return R.layout.layout_more_dialog;
    }

    @Override
    public void bindView(final View v) {
        RecyclerView rvThird = (RecyclerView) v.findViewById(R.id.rv_more_dialog_third_part);
        rvSystem = (RecyclerView) v.findViewById(R.id.rv_more_dialog_system);

        rvThird.setLayoutManager(new LinearLayoutManager(v.getContext(), LinearLayoutManager.HORIZONTAL, false));
        rvSystem.setLayoutManager(new LinearLayoutManager(v.getContext(), LinearLayoutManager.HORIZONTAL, false));

        List<IconMore> thirdList = new ArrayList<>();
        List<IconMore> systemList = new ArrayList<>();

        thirdList.add(new IconMore(R.drawable.icon_wechat, "微信", SHARE_TYPE_WECHAT));
        thirdList.add(new IconMore(R.drawable.icon_wechat_circle, "朋友圈", SHARE_TYPE_WECHAT_CIRCLE));
        thirdList.add(new IconMore(R.drawable.icon_weibo, "微博", SHARE_TYPE_WEIBO));
        thirdList.add(new IconMore(R.drawable.icon_qq, "QQ", SHARE_TYPE_QQ));
        thirdList.add(new IconMore(R.drawable.icon_qzone, "QQ空间", SHARE_TYPE_QZONE));
        thirdList.add(new IconMore(R.drawable.icon_more, "收藏", SHARE_TYPE_COLLECT));


        systemList.add(new IconMore(R.drawable.icon_dialog_browser, "用浏览器", SHARE_TYPE_BROWSER));
        systemList.add(new IconMore(R.drawable.icon_dialog_link, "复制链接", SHARE_TYPE_COPY_LINK));
        systemList.add(new IconMore(R.drawable.icon_dialog_fresh, "刷新", SHARE_TYPE_FRESH));
        if (isShowCollect) {
            systemList.add(new IconMore(R.drawable.icon_dialog_un_collect, "收藏", SHARE_TYPE_COLLECT));
        }
        systemList.add(new IconMore(R.drawable.icon_dialog_more, "分享", SHARE_TYPE_SHARE));

        MoreListAdapter thirdAdapter = new MoreListAdapter(thirdList);
        MoreListAdapter systemAdapter = new MoreListAdapter(systemList);

        rvThird.setAdapter(thirdAdapter);
        rvSystem.setAdapter(systemAdapter);

        v.findViewById(R.id.tv_more_dialog_cancle).setOnClickListener(new View.OnClickListener() {
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

    @Override
    public void onResume() {
        super.onResume();
    }


    class MoreListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {


        private List<IconMore> mList;


        public MoreListAdapter(List<IconMore> list) {
            mList = list;
        }

        public void setList(List<IconMore> list) {
            mList.clear();
            mList.addAll(list);
            notifyDataSetChanged();
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

            return new ContentVH(inflate(parent, R.layout.item_more_dialog_icon));
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            ((ContentVH) holder).setData(mList.get(position));
        }

        @Override
        public int getItemCount() {
            return mList == null ? 0 : mList.size();
        }


        private View inflate(ViewGroup parent, int layoutRes) {
            return LayoutInflater.from(parent.getContext()).inflate(layoutRes, parent, false);
        }


        public class ContentVH extends BaseViewHolder {

            @BindView(R.id.iv_item_more_dialog_image)
            ImageView ivIcon;

            @BindView(R.id.tv_item_more_dialog_name)
            TextView tvName;

            public ContentVH(View itemView) {
                super(itemView);
                AutoUtils.autoSize(itemView);
                ButterKnife.bind(this, itemView);
            }

            public void setData(final IconMore iconEntity) {
                if (iconEntity.getType() == SHARE_TYPE_COLLECT) {
                    if (User.isLogin()) {
                        setVisibility(true);
                        ivIcon.setImageResource(isCollect ? R.drawable.icon_dialog_collect : R.drawable.icon_dialog_un_collect);
                        tvName.setText(isCollect ? "已收藏" : iconEntity.getName());
                    }else {
                        setVisibility(false);
                    }

                } else {
                    ivIcon.setImageResource(iconEntity.getImage());
                    tvName.setText(iconEntity.getName());
                }


                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        switch (iconEntity.getType()) {
                            case MoreDialog.SHARE_TYPE_WECHAT:
                                break;
                            case MoreDialog.SHARE_TYPE_WECHAT_CIRCLE:

                                break;
                            case MoreDialog.SHARE_TYPE_QQ:

                                break;
                            case MoreDialog.SHARE_TYPE_QZONE:
                                break;
                            default:
                                if (mListener != null) {
                                    mListener.onDialogSelect(iconEntity.getType());
                                    dismiss();
                                }
                                break;
                        }


                    }
                });
            }
        }


    }

    public class IconMore {
        private int type;
        private int image;
        private String name;

        public IconMore(int image, String name, int type) {
            this.image = image;
            this.name = name;
            this.type = type;
        }

        public int getType() {
            return type;
        }

        public void setType(int type) {
            this.type = type;
        }

        public int getImage() {
            return image;
        }

        public void setImage(int image) {
            this.image = image;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }

    public interface OnDialogSelectListener {
        void onDialogSelect(int type);
    }
}
