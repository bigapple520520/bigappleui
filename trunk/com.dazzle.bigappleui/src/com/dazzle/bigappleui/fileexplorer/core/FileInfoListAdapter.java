/* 
 * @(#)FileInfoListAdapter.java    Created on 2014-12-9
 * Copyright (c) 2014 ZDSoft Networks, Inc. All rights reserved.
 * $Id$
 */
package com.dazzle.bigappleui.fileexplorer.core;

import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.dazzle.bigappleui.fileexplorer.entity.FileInfo;
import com.dazzle.bigappleui.fileexplorer.entity.FileInfoListItemView;
import com.dazzle.bigappleui.fileexplorer.widget.FileExplorerUIHepler;

public class FileInfoListAdapter extends BaseAdapter {
    private List<FileInfo> fileInfoList;
    private Context context;

    /** 选中的文件 */
    private List<String> selectedFilePathList;
    /** 选中ImageView被点击监听 */
    private SelectImageViewOnClickListener l;
    /** 是否多选 */
    private boolean ifMultiple;

    public FileInfoListAdapter(Context context, List<FileInfo> fileInfoList, List<String> selectedFilePathList,
            SelectImageViewOnClickListener l, boolean ifMultiple) {
        this.fileInfoList = fileInfoList;
        this.context = context;
        this.selectedFilePathList = selectedFilePathList;
        this.l = l;
        this.ifMultiple = ifMultiple;
    }

    @Override
    public int getCount() {
        return fileInfoList.size();
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        // 缓存
        FileInfoListItemView cacheView = null;
        if (null != view) {
            cacheView = (FileInfoListItemView) view.getTag();
        }
        else {
            cacheView = FileExplorerUIHepler.getFileInfoListItemView((Activity) context);
            cacheView.root.setTag(cacheView);
        }

        final FileInfo fileInfo = fileInfoList.get(position);
        final FileInfoListItemView fileInfoListItemView = cacheView;

        fileInfoListItemView.textView2.setText(Util.getDateByLong(fileInfo.modifiedDate));
        fileInfoListItemView.fileIcon.setImageDrawable(fileInfo.icon);
        if (fileInfo.isDir) {
            fileInfoListItemView.textView1.setText(fileInfo.fileName + "(" + fileInfo.count + ")");
            fileInfoListItemView.selectImageView.setVisibility(View.GONE);
        }
        else {
            fileInfoListItemView.textView1.setText(fileInfo.fileName);

            fileInfoListItemView.selectImageView.setVisibility(View.VISIBLE);
            if (selectedFilePathList.contains(fileInfo.filePath)) {
                fileInfoListItemView.selectImageView.setImageDrawable(DrawableHelper.getCheckBoxSelectedIcon());
            }
            else {
                fileInfoListItemView.selectImageView.setImageDrawable(DrawableHelper.getCheckBoxNormalIcon());
            }
            fileInfoListItemView.selectImageView.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (null != l) {
                        l.onClick(fileInfoListItemView.selectImageView, fileInfo);
                    }
                }
            });
        }

        if (!ifMultiple) {
            // 单选不显示选择框
            fileInfoListItemView.selectImageView.setVisibility(View.GONE);
        }
        return fileInfoListItemView.root;
    }

    @Override
    public Object getItem(int position) {
        return fileInfoList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    /**
     * 选中事件被点击监听
     * 
     * @author xuan
     * @version $Revision: 1.0 $, $Date: 2014-12-10 下午7:54:33 $
     */
    public interface SelectImageViewOnClickListener {
        void onClick(ImageView imageView, FileInfo fileInfo);
    }

}
