package com.gank.android.app.ui.main;

import android.app.Activity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.gank.android.app.R;
import com.gank.android.app.ui.classify.ClassifyFragment;
import com.gank.android.app.ui.mine.MineFragment;
import com.gank.android.app.ui.today.TodayFragment;


/**
 * @author shijunxing
 */

public enum UIPager {

    HOME(0, "今日",   TodayFragment.class,     R.drawable.tab_today_selector),
    FIND(1, "分类",   ClassifyFragment.class, R.drawable.tab_category_selector),
    MINE(2, "我的",   MineFragment.class,     R.drawable.tab_mine_selector);

    private int pagerIndex = 0;
    private int subPagerIndex = 0;
    private String label = null;
    private Class pager = null;
    private int icon = -1;

    UIPager(int pagerIndex, String label, Class pager, int icon) {
        this.pagerIndex = pagerIndex;
        this.label = label;
        this.pager = pager;
        this.icon = icon;
    }

    public int getPagerIndex() {
        return pagerIndex;
    }

    public String getLabel() {
        return label;
    }

    public Class getPager() {
        return pager;
    }

    public int getPagerIcon() {
        return icon;
    }

    public View getTabView(Activity context) {
        View tabView = context.getLayoutInflater().inflate(R.layout.layout_tab_item, null);
        TextView labelTxt = (TextView) tabView.findViewById(R.id.label);
        ImageView labelIcon = (ImageView) tabView.findViewById(R.id.icon);
        labelTxt.setText(getLabel());
        labelIcon.setImageResource(getPagerIcon());
        return tabView;
    }

    public int getSubPagerIndex() {
        return subPagerIndex;
    }

    public void setSubPagerIndex(int subPagerIndex) {
        this.subPagerIndex = subPagerIndex;
    }

    public static UIPager getType(int key) {
        for (UIPager value : values()) {
            if (key == value.pagerIndex) {
                return value;
            }
        }
        return HOME;
    }
    public static UIPager getType(String name) {
        for (UIPager value : values()) {
            if (value.name().equals(name)) {
                return value;
            }
        }
        return HOME;
    }
}
