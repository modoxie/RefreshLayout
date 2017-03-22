package com.base.refreshlayout.magnet;

import android.view.View;

/**
 * Created by Administrator on 2017/3/22
 */

public interface RefreshViewMagnet {
    void onChangStatus(int newStatus, float scale);
    View getView();
}
