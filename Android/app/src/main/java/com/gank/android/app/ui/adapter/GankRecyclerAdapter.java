package com.gank.android.app.ui.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.os.Build;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.blankj.utilcode.util.ScreenUtils;
import com.gank.android.app.R;
import com.gank.android.app.entity.GanHuoEntity;
import com.mcxtzhang.swipemenulib.SwipeMenuLayout;
import com.zhy.autolayout.utils.AutoUtils;

import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.gank.androidlibs.image.config.SingleConfig;
import cn.gank.androidlibs.image.loader.ImageLoader;
import cn.gank.androidlibs.kit.DateUtils;
import cn.gank.androidlibs.log.XLog;

/**
 * @author shijunxing
 */
public  class GankRecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private float mScrollMultiplier = 0.5f;

    public static class VIEW_TYPES {
        public static final int HEADER = -1;
        public static final int FIRST_VIEW = -2;
    }


    public interface OnParallaxScroll {
        /**
         * Event triggered when the parallax is being scrolled.
         */
        void onParallaxScroll(float percentage, float offset, View parallax);
    }

    private Context mContext;
    private List<GanHuoEntity> mData;
    private CustomRelativeWrapper mHeader;
    private OnListClickListener mListener;
    private OnParallaxScroll mParallaxScroll;
    private RecyclerView mRecyclerView;
    private boolean mShouldClipView = true;


    private boolean isShowTag = false;
    private boolean isShowCancel = false;

    public void setShowCancel(boolean showCancel) {
        isShowCancel = showCancel;
    }

    public void setShowTag(boolean showTag) {
        isShowTag = showTag;
    }
    public GankRecyclerAdapter(Context context, OnListClickListener listener) {
        mContext = context;
        mData = new ArrayList<>();
        this.mListener = listener;
    }
    /**
     * Translates the adapter in Y
     *
     * @param of offset in px
     */
    public void translateHeader(float of) {
        float ofCalculated = of * mScrollMultiplier;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB && of < mHeader.getHeight()) {
            mHeader.setTranslationY(ofCalculated);
        } else if (of < mHeader.getHeight()) {
            TranslateAnimation anim = new TranslateAnimation(0, 0, ofCalculated, ofCalculated);
            anim.setFillAfter(true);
            anim.setDuration(0);
            mHeader.startAnimation(anim);
        }
        mHeader.setClipY(Math.round(ofCalculated));
        if (mParallaxScroll != null) {
            final RecyclerView.ViewHolder holder = mRecyclerView.findViewHolderForAdapterPosition(0);
            float left;
            if (holder != null) {
                left = Math.min(1, ((ofCalculated) / (mHeader.getHeight() * mScrollMultiplier)));
            }else {
                left = 1;
            }
            mParallaxScroll.onParallaxScroll(left, of, mHeader);
        }
    }

    /**
     * Set the view as header.
     *
     * @param header The inflated header
     * @param view   The RecyclerView to set scroll listeners
     */
    public void setParallaxHeader(View header, final RecyclerView view) {
        mRecyclerView = view;
        mHeader = new CustomRelativeWrapper(header.getContext(), mShouldClipView);
        mHeader.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        mHeader.addView(header, new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        view.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (mHeader != null) {
                    translateHeader(mRecyclerView.getLayoutManager().getChildAt(0) == mHeader ?
                            mRecyclerView.computeVerticalScrollOffset() : mHeader.getHeight());

                }
            }
        });
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {

        int type = getItemViewType(position);
        int dataIndex = position;
        if (mHeader != null) {
            dataIndex--;
        }
        switch (type) {

            case VIEW_TYPES.HEADER:
                break;
            case GanHuoEntity.TITLE:
            case VIEW_TYPES.FIRST_VIEW:
                ((TitleVH) holder).setData(getData().get(dataIndex));
                break;
            case GanHuoEntity.CONTENT:
                ((ContentVH) holder).setData(getData().get(dataIndex), dataIndex);
                break;
            case GanHuoEntity.WELFARE:
                ((WelfareVH) holder).setData(getData().get(dataIndex));
                break;
            default:
                break;
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, final int viewType) {
        XLog.d("onCreateViewHolder","type:"+viewType);

        if (viewType == VIEW_TYPES.HEADER && mHeader != null) {
            return new ViewHolder(mHeader);
        }
        if (viewType == VIEW_TYPES.FIRST_VIEW && mHeader != null && mRecyclerView != null) {
            final RecyclerView.ViewHolder holder = mRecyclerView.findViewHolderForAdapterPosition(0);
            if (holder != null) {
                translateHeader(-holder.itemView.getTop());
            }
        }
        switch (viewType) {
            case GanHuoEntity.TITLE:
            case VIEW_TYPES.FIRST_VIEW:
                return new TitleVH(inflate(viewGroup, R.layout.item_today_list_title));
            case GanHuoEntity.CONTENT:
                return new ContentVH(inflate(viewGroup, R.layout.item_today_list_content));
            case GanHuoEntity.WELFARE:
                return new WelfareVH(inflate(viewGroup, R.layout.item_welfare_list));
            default:
                throw new InvalidParameterException();
        }


    }

    /**
     * @return true if there is a header on this adapter, false otherwise
     */
    public boolean hasHeader() {
        return mHeader != null;
    }

    public boolean isShouldClipView() {
        return mShouldClipView;
    }

    /**
     * Defines if we will clip the layout or not. MUST BE CALLED BEFORE {@link
     * #setParallaxHeader(android.view.View, android.support.v7.widget.RecyclerView)}
     */
    public void setShouldClipView(boolean shouldClickView) {
        mShouldClipView = shouldClickView;
    }

    public void setOnParallaxScroll(OnParallaxScroll parallaxScroll) {
        mParallaxScroll = parallaxScroll;
        mParallaxScroll.onParallaxScroll(0, 0, mHeader);
    }

    public GankRecyclerAdapter(List<GanHuoEntity> data) {
        mData = data;
    }

    public List<GanHuoEntity> getData() {
        return mData;
    }

    public void setData(List<GanHuoEntity> data) {
        mData.clear();
        mData.addAll(data);
        notifyDataSetChanged();
    }

    public void addData(List<GanHuoEntity> data) {
        int start = mData.size();
        mData.addAll(data);
        notifyItemRangeInserted(start, mData.size());
    }

    public void addItem(GanHuoEntity item, int position) {
        mData.add(position, item);
        notifyItemInserted(position + (mHeader == null ? 0 : 1));
    }

    public void removeItem(GanHuoEntity item) {
        int position = mData.indexOf(item);
        if (position < 0) {
            return;
        }
        mData.remove(item);
        notifyItemRemoved(position + (mHeader == null ? 0 : 1));
    }


    @Override
    public int getItemCount() {
        return getData().size() + (mHeader == null ? 0 : 1);

    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0 && mHeader != null) {
            return VIEW_TYPES.HEADER;
        }
        if (position == 1 && mHeader != null) {
            return VIEW_TYPES.FIRST_VIEW;
        }
        if (getData() != null && getData().size() != 0) {

            int dataIndex = position;
            if (mHeader != null) {
                dataIndex = position - 1;
            }

            if (getData().get(dataIndex).getItemType() == 1) {
                return GanHuoEntity.TITLE;
            }
            if (getData().get(dataIndex).getItemType() == 2) {
                return GanHuoEntity.CONTENT;
            }
            if (getData().get(dataIndex).getItemType() == 3) {
                if (!isShowTag) {
                    return GanHuoEntity.WELFARE;
                } else {
                    return GanHuoEntity.CONTENT;
                }

            }
        }
        return -3;
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        public ViewHolder(View itemView) {
            super(itemView);
        }
    }


    private View inflate(ViewGroup parent, int layoutRes) {
        return LayoutInflater.from(mContext).inflate(layoutRes, parent, false);
    }


    /**
     * 福利
     */
    public class WelfareVH extends RecyclerView.ViewHolder {

        @BindView(R.id.iv_item_welfare_list_image)
        ImageView ivImage;

        @BindView(R.id.tv_item_welfare_list_time)
        TextView tvTime;

        public WelfareVH(View itemView) {
            super(itemView);
            AutoUtils.autoSize(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void setData(GanHuoEntity ganHuoEntity) {

            ImageLoader.with(mContext)
                    .placeHolder(R.drawable.holder_small_image)
                    .error(R.drawable.holder_small_image)
                    .url(ganHuoEntity.getUrl())
                    .asBitmap(new SingleConfig.BitmapListener() {
                        @Override
                        public void onSuccess(Bitmap bitmap) {
                            ViewGroup.LayoutParams params = ivImage.getLayoutParams();
                            params.height = ScreenUtils.getScreenWidth() / 2;
                            params.width = ScreenUtils.getScreenWidth() / 2;
                            ivImage.setLayoutParams(params);
                            ivImage.setImageBitmap(bitmap);
                        }

                        @Override
                        public void onFail() {

                        }
                    });

            tvTime.setText(ganHuoEntity.getDate());
            ivImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    mListener.goToImages(getData(), getAdapterPosition());
                }
            });
        }
    }

    /**
     * 标题
     */
    public class TitleVH extends RecyclerView.ViewHolder {

        @BindView(R.id.tv_item_today_list_title)
        TextView tvTitle;

        public TitleVH(View itemView) {
            super(itemView);
            AutoUtils.autoSize(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void setData(final GanHuoEntity ganHuoEntity) {
            tvTitle.setText(ganHuoEntity.getType());
            tvTitle.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mListener.gotToClassify(ganHuoEntity.getType());
                }
            });
        }
    }

    /**
     * 内容
     */
    public class ContentVH extends RecyclerView.ViewHolder {

        @BindView(R.id.tv_item_today_list_time)
        TextView tvTime;

        @BindView(R.id.tv_item_today_list_content)
        TextView tvContent;

        @BindView(R.id.tv_item_today_list_author)
        TextView tvAuthor;

        @BindView(R.id.tv_item_today_list_tag)
        TextView tvTag;

        @BindView(R.id.content)
        View vContent;

        @BindView(R.id.tv_item_today_list_cancel)
        View vDel;


        public ContentVH(View itemView) {
            super(itemView);
            AutoUtils.autoSize(itemView);
            ButterKnife.bind(this, itemView);
            if (isShowCancel) {
                ((SwipeMenuLayout) itemView).setLeftSwipe(true);
            } else {
                ((SwipeMenuLayout) itemView).setSwipeEnable(false);
            }

        }

        public void setData(final GanHuoEntity ganHuoEntity, final int position) {
            tvTime.setText(DateUtils.friendlyTime(ganHuoEntity.getPublishedAt().replace('T', ' ').replace('Z', ' ')));
            tvContent.setText(ganHuoEntity.getDesc());
            tvAuthor.setText("via. " + ganHuoEntity.getWho());
            if (isShowTag) {
                tvTag.setVisibility(View.VISIBLE);
                tvTag.setText(ganHuoEntity.getType());
                switch (ganHuoEntity.getType()) {

                    case "Android":
                        tvTag.setBackgroundColor(itemView.getResources().getColor(R.color.color_tag_android));
                        break;
                    case "iOS":
                        tvTag.setBackgroundColor(itemView.getResources().getColor(R.color.color_tag_ios));
                        break;
                    case "App":
                        tvTag.setBackgroundColor(itemView.getResources().getColor(R.color.color_tag_app));
                        break;
                    case "前端":
                        tvTag.setBackgroundColor(itemView.getResources().getColor(R.color.color_tag_h5));
                        break;
                    case "休息视频":
                        tvTag.setBackgroundColor(itemView.getResources().getColor(R.color.color_tag_vedio));
                        break;
                    case "拓展资源":
                        tvTag.setBackgroundColor(itemView.getResources().getColor(R.color.color_tag_resurce));
                        break;
                    case "瞎推荐":
                        tvTag.setBackgroundColor(itemView.getResources().getColor(R.color.color_tag_recommon));
                        break;
                    case "福利":
                        tvTag.setBackgroundColor(itemView.getResources().getColor(R.color.color_tag_welfare));
                        break;
                    default:
                        break;
                }

            } else {
                tvTag.setVisibility(View.GONE);
            }
            vContent.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mListener != null) {
                        mListener.getToDetail(ganHuoEntity.getDesc(), ganHuoEntity.getUrl(), ganHuoEntity);
                    }
                }
            });


            vDel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mContext instanceof OnListDelecteListener) {
                        ((SwipeMenuLayout) itemView).smoothClose();
                        ((OnListDelecteListener) mContext).onDelete(ganHuoEntity.get_id(), ganHuoEntity);
                    }
                }
            });

        }
    }


    public interface OnListDelecteListener {
        void onDelete(String uid, GanHuoEntity ganHuoEntity);
    }

    public interface OnListClickListener {
        void getToDetail(String title, String link, GanHuoEntity ganHuoEntity);

        void goToImages(List<GanHuoEntity> ganks, int position);

        void gotToClassify(String tag);
    }

    static class CustomRelativeWrapper extends RelativeLayout {

        private int mOffset;
        private boolean mShouldClip;

        public CustomRelativeWrapper(Context context, boolean shouldClick) {
            super(context);
            mShouldClip = shouldClick;
        }

        @Override
        protected void dispatchDraw(Canvas canvas) {
            if (mShouldClip) {
                canvas.clipRect(new Rect(getLeft(), getTop(), getRight(), getBottom() + mOffset));
            }
            super.dispatchDraw(canvas);
        }

        public void setClipY(int offset) {
            mOffset = offset;
            invalidate();
        }
    }
    /**
     * Set parallax scroll multiplier.
     *
     * @param mul The multiplier
     */
    public void setScrollMultiplier(float mul) {
        this.mScrollMultiplier = mul;
    }

    /**
     * Get the current parallax scroll multiplier.
     *
     */
    public float getScrollMultiplier() {
        return this.mScrollMultiplier;
    }
}
