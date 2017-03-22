package com.base.refreshlayout.util;

import android.support.annotation.IdRes;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import java.util.HashMap;

/**
 * Created by Administrator on 2017/3/22
 */

public class ViewHolderRc extends RecyclerView.ViewHolder {
    HashMap<Integer, View> views = new HashMap<>();

    public ViewHolderRc(View itemView) {
        super(itemView);
    }

    public <T extends View> T find(@IdRes int id) {
        View view = views.get(id);
        if (view == null) {
            view = itemView.findViewById(id);
            views.put(id, view);
        }
        return (T) view;
    }

    public void clear() {
        views.clear();
    }
}
