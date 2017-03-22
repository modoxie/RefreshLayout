package com.base.refreshlayout.magnet;

import android.content.Context;
import android.graphics.Color;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.base.refreshlayout.R;
import com.base.refreshlayout.RefreshLayout;


/**
 * Created by Administrator on 2016/6/2
 */
public class DefultRefreshViewHold implements RefreshViewMagnet {
    public final static int HEAD_VIEW = 0x000001;
    public final static int BOTTOM_VIEW = 0x000002;
    private TextView view;

    public DefultRefreshViewHold(Context context, int mode) {
        view = new TextView(context);
        FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.WRAP_CONTENT);
        switch (mode) {
            case HEAD_VIEW:
                layoutParams.gravity = Gravity.TOP;
                view.setText("下拉刷新...");
                break;
            case BOTTOM_VIEW:
                layoutParams.gravity = Gravity.BOTTOM;
                view.setText("上拉加载更多...");
                break;
        }
        view.setLayoutParams(layoutParams);
        view.setTextColor(Color.parseColor("#435124"));
        view.setText("下拉刷新...");
        view.setGravity(Gravity.CENTER);
        view.setPadding(16, 16, 16, 16);
        view.setVisibility(View.INVISIBLE);
    }

    @Override
    public void onChangStatus(int newStatus, float scale) {
        TextView tv = (TextView) view;
        if (newStatus == RefreshLayout.loadMareStatue_end) {
            tv.setVisibility(View.INVISIBLE);
            return;
        }
        switch (newStatus) {
            case RefreshLayout.loadMareStatue_up:
                tv.setVisibility(View.VISIBLE);
                tv.setText("上拉加载更多");
                break;
            case RefreshLayout.loadMareStatue_pre:
                tv.setVisibility(View.VISIBLE);
                tv.setText("松开加载更多");
                break;
            case RefreshLayout.loadMareStatue_loading:
                tv.setVisibility(View.VISIBLE);
                tv.setText("加载中...");
                break;
            case RefreshLayout.loadMareStatue_down:
                tv.setVisibility(View.VISIBLE);
                tv.setText("下拉刷新");
                break;
            case RefreshLayout.loadMareStatue_r_pre:
                tv.setVisibility(View.VISIBLE);
                tv.setText("松开刷新");
                break;
            case RefreshLayout.loadMareStatue_r_loading:
                tv.setVisibility(View.VISIBLE);
                tv.setText("刷新中...");
                break;
            case RefreshLayout.loadMareStatue_no_more:
                tv.setVisibility(View.VISIBLE);
                tv.setText("亲，看完了");
                break;
        }
    }

    @Override
    public View getView() {
        return view;
    }
}
