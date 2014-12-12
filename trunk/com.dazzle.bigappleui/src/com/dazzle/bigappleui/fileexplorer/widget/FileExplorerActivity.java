/*
 * Copyright (c) 2010-2011, The MiCode Open Source Community (www.micode.net)
 *
 * This file is part of FileExplorer.
 *
 * FileExplorer is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * FileExplorer is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with SwiFTP.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.dazzle.bigappleui.fileexplorer.widget;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.Toast;

import com.dazzle.bigappleui.fileexplorer.core.DrawableHelper;
import com.dazzle.bigappleui.fileexplorer.core.FileInfoListAdapter;
import com.dazzle.bigappleui.fileexplorer.core.FileInfoListAdapter.SelectImageViewOnClickListener;
import com.dazzle.bigappleui.fileexplorer.core.FileSortHelper;
import com.dazzle.bigappleui.fileexplorer.core.Util;
import com.dazzle.bigappleui.fileexplorer.entity.FileExplorerActivityView;
import com.dazzle.bigappleui.fileexplorer.entity.FileInfo;
import com.winupon.andframe.bigapple.utils.StringUtils;

/**
 * 文件选择器界面
 * 
 * @author xuan
 * @version $Revision: 1.0 $, $Date: 2014-12-9 下午2:14:58 $
 */
public class FileExplorerActivity extends Activity {
    /** 返回结果在Intent对象中已这个key取值 */
    public static final String PARAM_RESULT = "param.result";
    /** 多选文件时限制文件上限 */
    public static final String PARAM_LIMIT_COUNT = "param.limiti.count";
    /** 判断是多选还是单选 */
    public static final String PARAM_IF_MULTIPLE_CHOICE = "param.if.multiple.choice";

    /** 文件数据列表 */
    private List<FileInfo> fileInfoList = new ArrayList<FileInfo>();
    /** 文件数据列表设配器 */
    private FileInfoListAdapter fileInfoListAdapter;

    /** 界面布局对象 */
    private FileExplorerActivityView root;

    /** 刚开始进入的默认文件路径 */
    private String showPath = Util.getSdDirectory();

    /** 文件排序帮助类 */
    private FileSortHelper fileSortHelper;

    /** 选中的文件列表 */
    private ArrayList<String> selectedFilePathList = new ArrayList<String>();

    /** 判断是否是多选模式，true表示多选，false表示单选即选择后马上返回 */
    private boolean ifMultiple;
    /** 多选时可限制选择文件数目，如果是-1表示可以无限制的选 */
    private int limitCount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        root = FileExplorerUIHepler.getFileExplorerActivity(this);
        setContentView(root.root);

        // 获取参数
        ifMultiple = getIntent().getBooleanExtra(PARAM_IF_MULTIPLE_CHOICE, true);
        if (!ifMultiple) {
            root.titleView.rightTextView.setVisibility(View.GONE);
        }
        limitCount = getIntent().getIntExtra(PARAM_LIMIT_COUNT, -1);

        // 返回事件
        root.titleView.leftTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                finish();
            }
        });

        // 点击完成
        root.titleView.rightTextView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                finishAndReturnData();
            }
        });

        fileSortHelper = new FileSortHelper();
        fileInfoListAdapter = new FileInfoListAdapter(this, fileInfoList, selectedFilePathList,
                new SelectImageViewOnClickListener() {
                    @Override
                    public void onClick(ImageView imageView, FileInfo fileInfo) {
                        if (ifMultiple) {
                            // 多选
                            if (selectedFilePathList.contains(fileInfo.filePath)) {
                                selectedFilePathList.remove(fileInfo.filePath);
                                imageView.setImageDrawable(DrawableHelper.getCheckBoxNormalIcon());
                            }
                            else {
                                if (-1 != limitCount && selectedFilePathList.size() >= limitCount) {
                                    // 到限制数量了，不让选了
                                    Toast.makeText(FileExplorerActivity.this, "最多只能选择" + limitCount + "个文件",
                                            Toast.LENGTH_LONG).show();
                                }
                                else {
                                    selectedFilePathList.add(fileInfo.filePath);
                                    imageView.setImageDrawable(DrawableHelper.getCheckBoxSelectedIcon());
                                }
                            }
                            refreshSelectedCount();
                        }
                    }
                }, ifMultiple);
        root.fileListView.setAdapter(fileInfoListAdapter);

        root.fileListView.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                FileInfo fileInfo = (FileInfo) fileInfoListAdapter.getItem(position);
                if (fileInfo.isDir) {
                    // 文件夹进入
                    showPath = fileInfo.filePath;
                    refreshData();
                }
                else {
                    // 文件选择
                    if (!ifMultiple) {
                        // 单选
                        selectedFilePathList.add(fileInfo.filePath);
                        finishAndReturnData();
                    }
                }
            }
        });

        // 刷新数据
        refreshData();
        refreshSelectedCount();
    }

    /** 刷新数据 */
    private void refreshData() {
        if (TextUtils.isEmpty(showPath)) {
            return;
        }

        // 加载指定目录下的数据
        Util.reloadFileList(this, showPath, fileSortHelper, fileInfoList);
        fileInfoListAdapter.notifyDataSetChanged();
        if (fileInfoList.isEmpty()) {
            root.noDataTextView.setVisibility(View.VISIBLE);
        }
        else {
            root.noDataTextView.setVisibility(View.GONE);
        }

        // 刷新导航
        refreshNavigation(showPath);
    }

    /** 刷新导航条 */
    private void refreshNavigation(String defaultPath) {
        if (TextUtils.isEmpty(defaultPath)) {
            return;
        }

        root.navigationLayout.removeAllViews();
        root.navigationLayout.addView(FileExplorerUIHepler.getNavigationItem(this, "当前位置：", null, null));

        root.navigationLayout.addView(FileExplorerUIHepler.getNavigationItem(this, "/root", "/", new OnClickListener() {
            @Override
            public void onClick(View view) {
                showPath = (String) view.getTag();
                refreshData();
            }
        }));
        if ("/".equals(defaultPath)) {
        }
        else {
            String[] items = StringUtils.split(defaultPath, "/");
            final StringBuilder filePathSb = new StringBuilder();
            for (String item : items) {
                filePathSb.append("/" + item);
                root.navigationLayout.addView(FileExplorerUIHepler.getNavigationItem(this, "/" + item,
                        filePathSb.toString(), new OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                showPath = (String) view.getTag();
                                refreshData();
                            }
                        }));
            }
        }
    }

    /** 回退操作 */
    private void doBack() {
        if (TextUtils.isEmpty(showPath) || "/".equals(showPath)) {
            finish();
        }
        else {
            int n = showPath.lastIndexOf("/");
            if (0 == n) {
                showPath = "/";
            }
            else {
                showPath = showPath.substring(0, n);
            }
            refreshData();
        }
    }

    /** 刷新已选择图片张数,如果数量大于0,设置确定按钮为可用,反之不可用并修改字体颜色 */
    private void refreshSelectedCount() {
        int currentSelected = selectedFilePathList.size();
        if (currentSelected > 0) {
            if (-1 == limitCount) {
                root.titleView.rightTextView.setText("完成 (" + currentSelected + ")");
            }
            else {
                root.titleView.rightTextView.setText("完成 (" + currentSelected + "/" + limitCount + ")");
            }

            root.titleView.rightTextView.setTextColor(0xFFFFFFFF);
            root.titleView.rightTextView.setEnabled(true);
            root.titleView.rightTextView.setClickable(true);
        }
        else {
            root.titleView.rightTextView.setText("完成");
            root.titleView.rightTextView.setTextColor(0x59ffffff);
            root.titleView.rightTextView.setEnabled(false);
            root.titleView.rightTextView.setClickable(false);
        }
    }

    /** 结束界面并返回数据 */
    private void finishAndReturnData() {
        Intent data = getIntent();
        data.putStringArrayListExtra(PARAM_RESULT, selectedFilePathList);
        setResult(RESULT_OK, data);
        FileExplorerActivity.this.finish();
    }

    @Override
    public void onBackPressed() {
        doBack();
    }

}
