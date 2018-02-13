package com.gank.android.app.ui.today;

import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.gank.android.app.R;
import com.gank.android.app.ui.base.BaseActivity;
import com.gank.android.app.even.BaseEven;
import com.gank.android.app.even.EvenBusTag;
import com.google.gson.reflect.TypeToken;
import com.haibin.calendarview.Calendar;
import com.haibin.calendarview.CalendarView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import cn.gank.androidlibs.event.BusFactory;
import cn.gank.androidlibs.httphelper.Convert;

/**
 * @author shijunxing
 * @date 2017/9/14
 */

public class HistoryActivity extends BaseActivity implements CalendarView.OnDateSelectedListener,
        CalendarView.OnDateChangeListener {

    @BindView(R.id.tv_history_month)
    TextView mTvHistoryMonth;

    @BindView(R.id.tv_history_month_year)
    TextView mTvHistoryMonthYear;

    @BindView(R.id.calendarView)
    CalendarView mCalendarView;

    private List<String> mHistoryList;
    private List<Calendar> mHistoryCalendar;
    private int mYear;

    public static final String HISTORY_LIST = "history_list";

    @Override
    public void initVariables() {
        String list = getIntent().getExtras().getString(HISTORY_LIST);
        if (list != null) {
            mHistoryList = Convert.fromJson(list, new TypeToken<ArrayList<String>>() {
            }.getType());
        }
    }

    @Override
    public void initViews(View view, Bundle savedInstanceState) {

        initToolBar("历史的车轮",false);
        mTvHistoryMonth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCalendarView.showSelectLayout(mYear);
            }
        });

        mCalendarView.setOnDateChangeListener(this);
        mCalendarView.setOnDateSelectedListener(this);
        mTvHistoryMonthYear.setText(String.valueOf(mCalendarView.getCurYear()));
        mYear = mCalendarView.getCurYear();
        mTvHistoryMonth.setText(mCalendarView.getCurMonth() + "月");

    }

    @Override
    public void loadData() {

        new GetSchemesTask().execute(mHistoryList);

    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_history;
    }


    private Calendar getSchemeCalendar(int year, int month, int day, int color) {
        Calendar calendar = new Calendar();
        calendar.setYear(year);
        calendar.setMonth(month);
        calendar.setDay(day);
        calendar.setMark(true);
        calendar.setSchemeColor(color);//如果单独标记颜色、则会使用这个颜色
        return calendar;
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onDateChange(Calendar calendar) {

        mTvHistoryMonth.setText(calendar.getMonth() + "月");
        mTvHistoryMonthYear.setText(String.valueOf(calendar.getYear()));
        mYear = calendar.getYear();
        getGankHuoCalendar(calendar.getYear(),calendar.getMonth());
    }

    @Override
    public void onDateSelected(Calendar calendar) {
        onDateChange(calendar);

        String month = calendar.getMonth() + "";
        String day = calendar.getDay() + "";
        if (month.length() == 1) {
            month = "0" + month;
        }
        if (day.length() == 1) {
            day = "0" + day;
        }
        String date = calendar.getYear() + "/" + month + "/" + day;
        if (mHistoryList.contains(date)) {
            BusFactory.getBus().post(new BaseEven(date, EvenBusTag.HISTORY_DATE));
            back();
        }

    }

    @Override
    public void onYearChange(int year) {
        mTvHistoryMonthYear.setText(String.valueOf(year));
    }


    private void getGankHuoCalendar(int year,int month){
        List<Calendar> calendars = new ArrayList<>();
        for (Calendar calendar : mHistoryCalendar) {
            if (calendar.getYear() == year && calendar.getMonth() == month){
                calendars.add(calendar);
            }
        }
        mCalendarView.setSchemeDate(calendars);

    }

    private class GetSchemesTask extends AsyncTask<List<String>, Object, List<Calendar>> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            getUiDelegate().showWaitDialog("加载中");
        }

        @SafeVarargs
        @Override
        protected final List<Calendar> doInBackground(List<String>... params) {
            List<Calendar> schemes = new ArrayList<>();
            for (String s : params[0]) {
                String dates[] = s.split("/");
                schemes.add(getSchemeCalendar(Integer.valueOf(dates[0]), Integer.valueOf(dates[1]), Integer.valueOf(dates[2]), getResources().getColor(R.color.colorThemeGolden)));
            }
            return schemes;
        }

        @Override
        protected void onPostExecute(List<Calendar> calendars) {
            super.onPostExecute(calendars);
            mHistoryCalendar = calendars;
            getGankHuoCalendar(mCalendarView.getCurYear(),mCalendarView.getCurMonth());
            getUiDelegate().hideWaitDialog();
        }
    }
}
