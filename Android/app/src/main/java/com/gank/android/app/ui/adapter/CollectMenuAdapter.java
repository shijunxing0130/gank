package com.gank.android.app.ui.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.gank.android.app.R;
import com.gank.android.app.entity.IconEntity;
import com.zhy.autolayout.utils.AutoUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by shijunxing on 2017/9/7
 */

public class CollectMenuAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {


    private List<String> mList;
    private OnListClickListener mListClickListener;


    public CollectMenuAdapter(OnListClickListener listClick) {
        mList = new ArrayList<>();
        this.mListClickListener = listClick;
    }

    public void setList(List<String> list) {
        mList.clear();
        mList.addAll(list);
        notifyDataSetChanged();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        return new ContentVH(inflate(parent, R.layout.item_collect_menu_icon));
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


    public class ContentVH extends RecyclerView.ViewHolder {


        @BindView(R.id.tv_item_classify_name)
        TextView tvName;

        public ContentVH(View itemView) {
            super(itemView);
            AutoUtils.autoSize(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void setData(final String name) {
            tvName.setText(name);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mListClickListener != null) {
                        mListClickListener.toClassifyList(name);
                    }
                }
            });
        }
    }


    public interface OnListClickListener{
        void toClassifyList(String tag);
    }
}
