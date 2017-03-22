package com.refreshlayout.example;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.TextView;

import com.base.refreshlayout.RefreshLayout;
import com.base.refreshlayout.magnet.DefultRefreshViewHold;
import com.base.refreshlayout.util.AdapterRc;
import com.base.refreshlayout.util.ViewHolderRc;

import java.util.Locale;


public class MainActivity extends AppCompatActivity {
    private RefreshLayout rl;
    private RecyclerView rv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        rl = (RefreshLayout) findViewById(R.id.rl);
        rl.init();
        rv = (RecyclerView) findViewById(R.id.rv);
        rv.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        rv.setAdapter(new AdapterRc() {
            @Override
            public ViewHolderRc onCreateViewHolder(ViewGroup parent, int viewType) {
                return new ViewHolderRc(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_text, parent, false));
            }

            @Override
            public void onBindViewHolder(ViewHolderRc holder, int position) {
                holder.<TextView>find(R.id.tv).setText(String.format(Locale.CHINA, "item:%d", position));
            }

            @Override
            public int getItemCount() {
                return 13;
            }
        });
    }

}
