/* 
 * @(#)FileExplorerDemo.java    Created on 2014-12-9
 * Copyright (c) 2014 ZDSoft Networks, Inc. All rights reserved.
 * $Id$
 */
package com.dazzle.bigappleui.demo.fileexplorer;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.DialogInterface;
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
import com.dazzle.bigappleui.fileexplorer.theme.DefaultFileExplorerTheme;
import com.dazzle.bigappleui.fileexplorer.theme.FileExplorerTheme;
import com.dazzle.bigappleui.fileexplorer.theme.FileExplorerThemeUtils;
import com.dazzle.bigappleui.fileexplorer.theme.custom.BlueFileExplorerTheme;
import com.dazzle.bigappleui.fileexplorer.theme.custom.GreenFileExplorerTheme;
import com.dazzle.bigappleui.fileexplorer.widget.FileExplorerActivity;
import com.winupon.andframe.bigapple.bitmap.local.LocalImageLoaderFace;
import com.winupon.andframe.bigapple.utils.AlertDialogUtils;

/**
 * 文件选择器DEMO
 * 
 * @author xuan
 * @version $Revision: 1.0 $, $Date: 2014-12-12 上午11:34:10 $
 */
public class FileExplorerDemo extends Activity {
    public static final int ACTIVITY_RESULT_1 = 1;
    public static final int ACTIVITY_RESULT_2 = 2;
    public static final int ACTIVITY_RESULT_3 = 3;

    private List<String> selPathList = new ArrayList<String>();
    private BaseAdapter adapter;

    private FileExplorerTheme fileExplorerTheme;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.demo_fileexplorer);
        LocalImageLoaderFace.init(this);
        // 选择主题
        final Button theme = (Button) findViewById(R.id.theme);
        theme.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                AlertDialogUtils.displayAlert4SingleChoice2(FileExplorerDemo.this, "请选择不同主题", true, new String[] {
                        "绿色", "蓝色", "默认" }, new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface arg0, int position) {
                        switch (position) {
                        case 0:
                            fileExplorerTheme = new GreenFileExplorerTheme();
                            break;
                        case 1:
                            fileExplorerTheme = new BlueFileExplorerTheme();
                            break;
                        case 2:
                            fileExplorerTheme = new DefaultFileExplorerTheme();
                            break;
                        }

                        theme.setBackgroundColor(fileExplorerTheme.titleBgColor());
                    }
                });
            }
        });

        // 单选
        Button danxuan = (Button) findViewById(R.id.danxuan);
        danxuan.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                FileExplorerThemeUtils.setTheme(fileExplorerTheme);// 设置主题
                FileExplorerUtils.gotoFileExplorerForSingle(FileExplorerDemo.this, ACTIVITY_RESULT_1);
            }
        });

        // 多选
        Button duoxuan = (Button) findViewById(R.id.duoxuan);
        duoxuan.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                FileExplorerThemeUtils.setTheme(fileExplorerTheme);// 设置主题
                // FileExplorerSettings.instance().setShowDotAndHiddenFiles(true);// 可显示一些系统文件，一般安全起见就不要开启
                FileExplorerUtils.gotoFileExplorerForForMulti(FileExplorerDemo.this, ACTIVITY_RESULT_2);
            }
        });

        // 多选有限制张数
        Button duoxuan2 = (Button) findViewById(R.id.duoxuan2);
        duoxuan2.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                FileExplorerThemeUtils.setTheme(fileExplorerTheme);// 设置主题
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
