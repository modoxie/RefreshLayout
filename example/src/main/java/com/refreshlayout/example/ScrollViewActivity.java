package com.refreshlayout.example;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.base.refreshlayout.RefreshLayout;
import com.base.refreshlayout.magnet.DefultRefreshViewHold;
import com.base.refreshlayout.util.ViewHolderRc;

import java.util.Locale;

/**
 * Created by Administrator on 2017/3/23
 */

public class ScrollViewActivity extends AppCompatActivity {
    private RefreshLayout rl;
    private LinearLayout ll;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scroll_view);
        ll = (LinearLayout) findViewById(R.id.ll);
        rl = (RefreshLayout) findViewById(R.id.rl);
        rl.setBackRefresh(new RefreshLayout.BackRefresh() {
            @Override
            public void refresh() {
                rl.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        rl.endRefresh();
                        ll.removeAllViews();
                        for (int i = 0; i < 13; i++) {
                            ViewHolderRc holder = new ViewHolderRc(LayoutInflater.from(ll.getContext()).inflate(R.layout.item_text, ll, false));
                            holder.<TextView>find(R.id.tv).setText(String.format(Locale.CHINA, "item:%d", i * 2));
                            ll.addView(holder.itemView);
                        }
                    }
                }, 2000);
            }
        }).init();
        for (int i = 0; i < 2; i++) {
            ViewHolderRc holder = new ViewHolderRc(LayoutInflater.from(ll.getContext()).inflate(R.layout.item_text, ll, false));
            holder.<TextView>find(R.id.tv).setText(String.format(Locale.CHINA, "item:%d", i));
            ll.addView(holder.itemView);
        }
    }
}
