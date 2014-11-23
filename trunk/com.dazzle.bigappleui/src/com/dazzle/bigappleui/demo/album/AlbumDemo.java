/* 
 * @(#)AlbumDemo.java    Created on 2014-11-12
 * Copyright (c) 2014 ZDSoft Networks, Inc. All rights reserved.
 * $Id$
 */
package com.dazzle.bigappleui.demo.album;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.dazzle.bigappleui.R;
import com.dazzle.bigappleui.album.AlbumUtils;
import com.dazzle.bigappleui.album.core.AlbumConfig;
import com.dazzle.bigappleui.album.entity.BucketImage;
import com.winupon.andframe.bigapple.bitmap.BitmapDisplayConfig;
import com.winupon.andframe.bigapple.bitmap.local.LocalImageLoaderFace;

/**
 * 相册选择测试
 * 
 * @author xuan
 * @version $Revision: 1.0 $, $Date: 2014-11-12 下午1:29:05 $
 */
public class AlbumDemo extends Activity {
    public static final int ACTIVITY_RESULT_1 = 1;
    public static final int ACTIVITY_RESULT_2 = 2;
    public static final int ACTIVITY_RESULT_3 = 3;

    private List<String> selPathList = new ArrayList<String>();
    private BaseAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.demo_album);
        LocalImageLoaderFace.init(this);

        // 单选
        Button danxuan = (Button) findViewById(R.id.danxuan);
        danxuan.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                AlbumUtils.gotoAlbumForSingle(AlbumDemo.this, ACTIVITY_RESULT_1);
            }
        });

        // 多选
        Button duoxuan = (Button) findViewById(R.id.duoxuan);
        duoxuan.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                AlbumUtils.gotoAlbumForMulti(AlbumDemo.this, ACTIVITY_RESULT_2);
            }
        });

        // 多选有限制张数
        Button duoxuan2 = (Button) findViewById(R.id.duoxuan2);
        duoxuan2.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                // 限制只能选3张
                AlbumUtils.gotoAlbumForMulti(AlbumDemo.this, 3, ACTIVITY_RESULT_2);
            }
        });

        // 图片存放
        GridView gridView = (GridView) findViewById(R.id.gridView);
        adapter = new BaseAdapter() {
            @Override
            public View getView(int position, View arg1, ViewGroup arg2) {
                RelativeLayout root = new RelativeLayout(AlbumDemo.this);
                ImageView imageView = new ImageView(AlbumDemo.this);
                RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(200, 200);
                imageView.setLayoutParams(lp);
                root.addView(imageView);

                BitmapDisplayConfig config = new BitmapDisplayConfig();
                config.setBitmapMaxHeight(200);
                config.setBitmapMaxWidth(200);
                LocalImageLoaderFace.display(imageView, selPathList.get(position), config);
                return root;
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
        gridView.setAdapter(adapter);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (RESULT_OK != resultCode) {
            return;
        }

        switch (requestCode) {
        case ACTIVITY_RESULT_1:
            List<BucketImage> temp1 = AlbumConfig.getSelListAndClear();
            for (BucketImage bi : temp1) {
                selPathList.add(bi.imagePath);
            }
            break;
        case ACTIVITY_RESULT_2:
            List<BucketImage> temp2 = AlbumConfig.getSelListAndClear();
            for (BucketImage bi : temp2) {
                selPathList.add(bi.imagePath);
            }
            break;
        case ACTIVITY_RESULT_3:
            List<BucketImage> temp3 = AlbumConfig.getSelListAndClear();
            for (BucketImage bi : temp3) {
                selPathList.add(bi.imagePath);
            }
            break;
        }
        adapter.notifyDataSetChanged();
    }

}
