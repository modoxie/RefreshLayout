package com.refreshlayout.example;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.base.refreshlayout.RefreshLayout;
import com.base.refreshlayout.magnet.DefultRefreshViewHold;
import com.base.refreshlayout.magnet.RefreshViewMagnet;
import com.base.refreshlayout.util.AdapterRc;
import com.base.refreshlayout.util.ViewHolderRc;

import java.util.Locale;


public class MainActivity extends AppCompatActivity {
    private RefreshLayout rl;
    private int listSize = 13;


    private BaseAdapter adapter;
//    private RecyclerView.Adapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        rl = (RefreshLayout) findViewById(R.id.rl);
        rl.setBackRefresh(new RefreshLayout.BackRefresh() {
            @Override
            public void refresh() {
                rl.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        listSize = 13;
                        rl.endRefresh();
                        adapter.notifyDataSetChanged();
                    }
                }, 2000);
            }
        }).setBackLoadMore(new RefreshLayout.BackLoadMore() {
            @Override
            public void loadMore() {
                rl.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (listSize > 26) {
                            rl.endLoadMore(false);
                        } else {
                            listSize += 10;
                            rl.endLoadMore(true);
                            adapter.notifyDataSetChanged();
                        }
                    }
                }, 2000);
            }
        }).setBackNoMore(new RefreshLayout.BackNoMore() {
            @Override
            public void noMore() {
                Toast.makeText(MainActivity.this, "没有更多数据", Toast.LENGTH_SHORT).show();
            }
        }).setBottomRefreshViewMagnet(new DefultRefreshViewHold(this, DefultRefreshViewHold.BOTTOM_VIEW)).init();

        ListView lv = (ListView) findViewById(R.id.lv);
        lv.setAdapter(adapter = new BaseAdapter() {
            @Override
            public int getCount() {
                return listSize;
            }

            @Override
            public Object getItem(int position) {
                return String.format(Locale.CHINA, "item:%d", position);
            }

            @Override
            public long getItemId(int position) {
                return position;
            }

            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                ViewHolderRc holder;
                if (convertView == null) {
                    holder = new ViewHolderRc(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_text, parent, false));
                    convertView = holder.itemView;
                    convertView.setTag(holder);
                } else {
                    holder = (ViewHolderRc) convertView.getTag();
                }
                holder.<TextView>find(R.id.tv).setText(getItem(position).toString());
                return holder.itemView;
            }
        });
/**
 * RecyclerView
 */
//        RecyclerView rv;
//        rv = (RecyclerView) findViewById(R.id.rv);
//        rv.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
//        rv.setAdapter(adapter = new AdapterRc() {
//            @Override
//            public ViewHolderRc onCreateViewHolder(ViewGroup parent, int viewType) {
//                return new ViewHolderRc(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_text, parent, false));
//            }
//
//            @Override
//            public void onBindViewHolder(ViewHolderRc holder, int position) {
//                holder.<TextView>find(R.id.tv).setText(String.format(Locale.CHINA, "item:%d", position));
//            }
//
//            @Override
//            public int getItemCount() {
//                return listSize;
//            }
//        });
    }

}
