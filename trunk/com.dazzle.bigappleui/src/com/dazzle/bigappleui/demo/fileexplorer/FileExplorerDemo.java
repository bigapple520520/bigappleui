/* 
 * @(#)FileExplorerDemo.java    Created on 2014-12-9
 * Copyright (c) 2014 ZDSoft Networks, Inc. All rights reserved.
 * $Id$
 */
package com.dazzle.bigappleui.demo.fileexplorer;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.dazzle.bigappleui.R;
import com.dazzle.bigappleui.fileexplorer.FileExplorerUtils;
import com.dazzle.bigappleui.fileexplorer.widget.FileExplorerActivity;
import com.winupon.andframe.bigapple.bitmap.local.LocalImageLoaderFace;

public class FileExplorerDemo extends Activity {
    public static final int ACTIVITY_RESULT_1 = 1;
    public static final int ACTIVITY_RESULT_2 = 2;
    public static final int ACTIVITY_RESULT_3 = 3;

    private List<String> selPathList = new ArrayList<String>();
    private BaseAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.demo_fileexplorer);
        LocalImageLoaderFace.init(this);

        // 单选
        Button danxuan = (Button) findViewById(R.id.danxuan);
        danxuan.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                FileExplorerUtils.gotoFileExplorerForSingle(FileExplorerDemo.this, ACTIVITY_RESULT_1);
            }
        });

        // 多选
        Button duoxuan = (Button) findViewById(R.id.duoxuan);
        duoxuan.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                FileExplorerUtils.gotoFileExplorerForForMulti(FileExplorerDemo.this, ACTIVITY_RESULT_2);
            }
        });

        // 多选有限制张数
        Button duoxuan2 = (Button) findViewById(R.id.duoxuan2);
        duoxuan2.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                FileExplorerUtils.gotoFileExplorerForForMulti(FileExplorerDemo.this, 3, ACTIVITY_RESULT_3);
            }
        });

        // 图片存放
        ListView listView = (ListView) findViewById(R.id.listView);
        adapter = new BaseAdapter() {
            @Override
            public View getView(int position, View arg1, ViewGroup arg2) {
                TextView t = new TextView(FileExplorerDemo.this);
                t.setText(selPathList.get(position));
                return t;
            }

            @Override
            public int getCount() {
                return selPathList.size();
            }

            @Override
            public long getItemId(int arg0) {
                return 0;
            }

            @Override
            public Object getItem(int arg0) {
                return null;
            }
        };
        listView.setAdapter(adapter);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (RESULT_OK != resultCode) {
            return;
        }

        switch (requestCode) {
        case ACTIVITY_RESULT_1:
            selPathList.addAll(data.getStringArrayListExtra(FileExplorerActivity.PARAM_RESULT));
            break;
        case ACTIVITY_RESULT_2:
            selPathList.addAll(data.getStringArrayListExtra(FileExplorerActivity.PARAM_RESULT));
            break;
        case ACTIVITY_RESULT_3:
            selPathList.addAll(data.getStringArrayListExtra(FileExplorerActivity.PARAM_RESULT));
            break;
        }
        adapter.notifyDataSetChanged();
    }

}
