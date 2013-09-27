package com.dazzle.bigappleui;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ArrayAdapter;

import com.dazzle.bigappleui.pull2refresh.EndRefreshListener;
import com.dazzle.bigappleui.pull2refresh.HeadRefreshListener;
import com.dazzle.bigappleui.pull2refresh.Pull2RefreshListView;
import com.dazzle.bigappleui.pull2refresh.Pull2RefreshUtils;

public class Main extends Activity {

    // 下拉刷新控件和设配器和数据
    private Pull2RefreshListView listView;

    // 模拟数据
    private ArrayAdapter<String> arrayAdapter;
    private final String[] data = new String[] { "aaa", "bbb", "ccc", "ddd", "eee", "fff", "ggg", "nnn", "mmm", "yyy",
            "iii" };
    private final String[] dataNew = new String[] { "111", "222", "333", "aaa", "bbb", "ccc", "ddd", "eee", "fff",
            "ggg", "nnn", "mmm", "yyy", "iii" };

    private final Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        arrayAdapter = new ArrayAdapter<String>(Main.this, android.R.layout.simple_list_item_1, data);
        listView = (Pull2RefreshListView) findViewById(R.id.listview);

        Pull2RefreshUtils.simpleInit(this, listView, arrayAdapter, handler, new HeadRefreshListener() {
            @Override
            public void headRefresh() {
                try {
                    Thread.sleep(5000);
                }
                catch (Exception e) {
                }
            }
        }, new EndRefreshListener() {
            @Override
            public void endRefresh() {
                try {
                    Thread.sleep(5000);
                }
                catch (Exception e) {
                }
            }
        });
    }

}
