package com.example.bigappleui_demo.pull2refresh;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;

import com.dazzle.bigappleui.pull2refresh.EndRefreshListener;
import com.dazzle.bigappleui.pull2refresh.HeadRefreshListener;
import com.dazzle.bigappleui.pull2refresh.PullToRefreshListView;
import com.dazzle.bigappleui.pull2refresh.PullToRefreshUtils;
import com.dazzle.bigappleui.utils.DateUtils;
import com.example.bigappleui_demo.R;

/**
 * 下拉刷新控件，使用时，需要引入<br>
 * 一个箭头图片：pull2refresh_arrow.png<br>
 * 一个头部布局文件：pull2refresh_footer.xml<br>
 * 一个尾部布局文件：pull2refresh_head.xml（这个不是必须的，只有想用尾部刷新时，引入即可）
 * 
 * @author xuan
 */
public class Main extends Activity {

    // 下拉刷新控件和设配器和数据
    private PullToRefreshListView listView;

    // 模拟数据
    private Pull2RefreshDemoAdapter pull2RefreshDemoAdapter;
    private List<String> dataList;

    private final Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pull2refresh_main);
        dataList = new ArrayList<String>();
        dataList.add("111");
        dataList.add("222");
        dataList.add("333");
        dataList.add("444");
        dataList.add("555");
        dataList.add("666");
        dataList.add("777");
        dataList.add("888");
        dataList.add("999");
        dataList.add("1212121");

        pull2RefreshDemoAdapter = new Pull2RefreshDemoAdapter(dataList, this);
        listView = (PullToRefreshListView) findViewById(R.id.listview);

        // 最简单的用法
        PullToRefreshUtils.simpleInit(this, listView, pull2RefreshDemoAdapter, handler, new HeadRefreshListener() {
            @Override
            public void headRefresh() {
                // 下拉刷新时会触发的事件，被线程调用，所以不用担心UI线程耗时问题，不过要在里面更新UI时，请用handler提交。
                try {
                    // 模拟耗时
                    Thread.sleep(3000);

                    // 这只需要加载数据，更新数据，数据设配器框架会帮你刷新。
                    dataList.add(0, DateUtils.date2StringBySecond(new Date()));
                }
                catch (Exception e) {
                }
            }
        }, new EndRefreshListener() {
            @Override
            public void endRefresh() {
                // 滑到底部会触发的事件，同上被线程调用。可设置成null，如果设置成null，就不使用滑到底部刷新功能。
                try {
                    // 模拟耗时
                    Thread.sleep(3000);

                    // 这只需要加载数据，更新数据，数据设配器框架会帮你刷新。
                    dataList.add(DateUtils.date2StringBySecond(new Date()));
                }
                catch (Exception e) {
                }
            }
        });
    }

}
