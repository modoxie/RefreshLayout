package com.base.refreshlayout;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.support.annotation.AttrRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StyleRes;
import android.support.v4.view.ScrollingView;
import android.support.v4.view.ViewCompat;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.ScrollView;

import com.base.refreshlayout.magnet.DefultRefreshViewHold;
import com.base.refreshlayout.magnet.RefreshViewMagnet;

/**
 * Created by Administrator on 2017/3/22
 */

public class RefreshLayout extends FrameLayout implements View.OnTouchListener {
    private View refreshView;
    private RefreshViewMagnet headView, bottomView;
    public final static int loadMareStatue_end = 0x000001;
    public final static int loadMareStatue_up = 0x000002;
    public final static int loadMareStatue_down = 0x000003;
    public final static int loadMareStatue_pre = 0x000004;
    public final static int loadMareStatue_r_pre = 0x000005;
    public final static int loadMareStatue_loading = 0x000006;
    public final static int loadMareStatue_r_loading = 0x000007;
    public final static int loadMareStatue_no_more = 0x000008;
    private int loadMareStatue = loadMareStatue_end;
    private float ry, offsetY;
    private BackRefresh backRefresh;
    private BackLoadMore backLoadMore;
    private BackNoMore backNoMore;
    boolean hasMore;

    public RefreshLayout(@NonNull Context context) {
        super(context);
    }

    public RefreshLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public RefreshLayout(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public RefreshLayout(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr, @StyleRes int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    public RefreshLayout setHeadRefreshViewMagnet(RefreshViewMagnet rvm) {
        headView = rvm;
        addView(headView.getView());
        return this;
    }

    public RefreshLayout setBottomRefreshViewMagnet(RefreshViewMagnet rvm) {
        bottomView = rvm;
        addView(bottomView.getView());
        return this;
    }

    public RefreshLayout setBackRefresh(BackRefresh backRefresh) {
        this.backRefresh = backRefresh;
        return this;
    }

    public RefreshLayout setBackLoadMore(BackLoadMore backLoadMore) {
        this.backLoadMore = backLoadMore;
        return this;
    }

    public RefreshLayout setBackNoMore(BackNoMore backNoMore) {
        this.backNoMore = backNoMore;
        return this;
    }

    public void init() {
        hasMore = true;
        if (headView == null) {
            headView = new DefultRefreshViewHold(getContext(), DefultRefreshViewHold.HEAD_VIEW);
            addView(headView.getView());
        }
        int children = getChildCount();
        for (int i = 0; i < children; i++) {
            View view = getChildAt(i);
            if (view instanceof RecyclerView
                    || view instanceof ListView
                    || view instanceof ScrollView
                    || view instanceof NestedScrollView) {
                refreshView = view;
                view.setOnTouchListener(this);
            }
        }
    }

    public int dip2px(float dipValue) {
        final float scale = getContext().getResources().getDisplayMetrics().density;
        return (int) (dipValue * scale + 0.5f);
    }

    @Override
    public boolean onTouch(View rv, MotionEvent event) {
        int dy;
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
            case MotionEvent.ACTION_MOVE:
                dy = (int) event.getRawY();
                if (ry == 0) {
                    ry = dy;
                    return false;
                }
                dy -= ry;
                if (loadMareStatue == loadMareStatue_end) {
                    if (dy < 0 && isBottom() && ViewCompat.canScrollVertically(rv, -1)) {
                        loadMareStatue = loadMareStatue_up;
                    } else if (dy > 0 && isTop() && offsetY == 0) {
                        loadMareStatue = loadMareStatue_down;
                    }
                    ry = event.getRawY();
                    return false;
                }
                if (dy < 0 && loadMareStatue == loadMareStatue_up || loadMareStatue == loadMareStatue_pre) {
                    if (bottomView != null) {
                        int height = bottomView.getView().getMeasuredHeight();
                        bottomView.getView().setTranslationY(dy > -height ? height + dy : 0);
                        rv.setTranslationY(dy > -height ? dy : -height);
                        bottomView.onChangStatus(loadMareStatue_up, (float) dy / -height);
                        loadMareStatue = loadMareStatue_up;
                        if (dy < -height) {
                            loadMareStatue = loadMareStatue_pre;
                            bottomView.onChangStatus(loadMareStatue_pre, 1);
                            return true;
                        }
                    } else if (dy < -dip2px(10)) {
                        loadMareStatue = loadMareStatue_pre;
                        return true;
                    }
                } else if (dy > 0 && loadMareStatue == loadMareStatue_down || loadMareStatue == loadMareStatue_r_pre) {
                    int height = headView.getView().getMeasuredHeight();
//                        float y = dy - height;
                    headView.getView().setTranslationY(dy < height ? dy - height : 0);
                    rv.setTranslationY(dy < height ? dy : height);
                    headView.onChangStatus(loadMareStatue_down, (float) dy / height);
                    loadMareStatue = loadMareStatue_down;
                    if (dy > height) {
                        loadMareStatue = loadMareStatue_r_pre;
                        headView.onChangStatus(loadMareStatue_r_pre, (float) dy / height);
                        return true;
                    }
                }
                break;
            case MotionEvent.ACTION_UP:
                int lms = loadMareStatue;
                if (lms == loadMareStatue_pre) {
                    loadMore();
                } else if (lms == loadMareStatue_r_pre) {
                    refresh();
                } else if (lms != loadMareStatue_loading && lms != loadMareStatue_r_loading) {
                    loadMareStatue = loadMareStatue_end;
                    ry = 0;
                    dy = 0;
                    rv.setTranslationY(0);
                    if (bottomView != null) bottomView.onChangStatus(loadMareStatue_end, 1);
                    headView.onChangStatus(loadMareStatue_end, 1);
                }
                break;
            default:
                loadMareStatue = loadMareStatue_end;
                ry = 0;
                dy = 0;
                rv.setTranslationY(0);
                if (bottomView != null) bottomView.onChangStatus(loadMareStatue_end, 1);
                headView.onChangStatus(loadMareStatue_end, 1);
        }
        return false;
    }

    public void refresh() {
        headView.onChangStatus(loadMareStatue_r_loading, 1);
        loadMareStatue = loadMareStatue_r_loading;
        if (backRefresh != null) {
            backRefresh.refresh();
        }
    }

    public void loadMore() {
        if (hasMore) {
            if (bottomView != null) bottomView.onChangStatus(loadMareStatue_loading, 1);
            loadMareStatue = loadMareStatue_loading;
            if (backLoadMore != null) {
                backLoadMore.loadMore();
            }
        } else {
            endLoadMore(false);
            if (backNoMore != null) {
                backNoMore.noMore();
            }
        }
    }

    public void endRefresh() {
        hasMore = true;
        ry = 0;
        refreshView.setTranslationY(0);
        loadMareStatue = loadMareStatue_end;
        if (headView != null) headView.onChangStatus(loadMareStatue_end, 1);
    }

    public void endLoadMore(boolean hasMore) {
        this.hasMore = hasMore;
        ry = 0;
        refreshView.setTranslationY(0);
        loadMareStatue = loadMareStatue_end;
        if (bottomView != null) bottomView.onChangStatus(loadMareStatue_end, 1);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    public boolean isTop() {
        if (refreshView instanceof RecyclerView) {
            RecyclerView.LayoutManager manager = ((RecyclerView) refreshView).getLayoutManager();
            if (manager instanceof LinearLayoutManager) {
                int index = ((LinearLayoutManager) manager).findFirstCompletelyVisibleItemPosition();
                if (index == -1) {
                    index = ((LinearLayoutManager) manager).findFirstVisibleItemPosition();
                }
                return manager.getChildCount() <= 0 || (index == 0 && manager.findViewByPosition(index).getTop() == manager.getTopDecorationHeight(manager.findViewByPosition(0)));
            } else if (manager instanceof StaggeredGridLayoutManager) {
                int min = 0;
                int[] last = ((StaggeredGridLayoutManager) manager).findFirstCompletelyVisibleItemPositions(null);
                for (int i = 0; i < last.length; i++) {
                    min = min > last[i] ? last[i] : min;
                }
                return min == 0;
            }
        } else if (refreshView instanceof ListView) {
            ListView lv = (ListView) refreshView;
            int index = lv.getFirstVisiblePosition();
            ListAdapter adapter = lv.getAdapter();
            return adapter.getCount() <= 0 || index == 0 && !ViewCompat.canScrollVertically(lv, -1);
        } else if (refreshView instanceof ScrollView) {

        } else if (refreshView instanceof NestedScrollView) {

        }
        return false;
    }

    public boolean isBottom() {
        if (refreshView instanceof RecyclerView) {
            RecyclerView.LayoutManager manager = ((RecyclerView) refreshView).getLayoutManager();
            if (((RecyclerView) refreshView).getChildCount() == 0) return true;
            if (manager instanceof LinearLayoutManager) {
                int last = ((LinearLayoutManager) manager).findLastCompletelyVisibleItemPosition();
                if (last == -1) {
                    last = ((LinearLayoutManager) manager).findLastVisibleItemPosition();
                }
                return last == manager.getItemCount() - 1 && manager.findViewByPosition(last).getBottom() + manager.getBottomDecorationHeight(manager.findViewByPosition(last)) == refreshView.getMeasuredHeight();
            } else if (manager instanceof StaggeredGridLayoutManager) {
                int max = -1;
                int[] last = ((StaggeredGridLayoutManager) manager).findLastCompletelyVisibleItemPositions(null);
                for (int i = 0; i < last.length; i++) {
                    max = max < last[i] ? last[i] : max;
                }
                return max == manager.getItemCount() - 1;
            }
        } else if (refreshView instanceof ListView) {
            ListView lv = (ListView) refreshView;
            int index = lv.getLastVisiblePosition();
            ListAdapter adapter = lv.getAdapter();
            return adapter.getCount() <= 0 || (index == adapter.getCount() - 1) && !ViewCompat.canScrollVertically(lv, 1);
        } else if (refreshView instanceof ScrollView) {

        } else if (refreshView instanceof NestedScrollView) {

        }
        return false;
    }

    public interface BackRefresh {
        void refresh();
    }

    public interface BackLoadMore {
        void loadMore();
    }

    public interface BackNoMore {
        void noMore();
    }
}
