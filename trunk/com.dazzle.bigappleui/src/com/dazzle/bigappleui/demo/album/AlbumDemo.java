/* 
 * @(#)AlbumDemo.java    Created on 2014-11-12
 * Copyright (c) 2014 ZDSoft Networks, Inc. All rights reserved.
 * $Id$
 */
package com.dazzle.bigappleui.demo.album;

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
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.dazzle.bigappleui.R;
import com.dazzle.bigappleui.album.AlbumUtils;
import com.dazzle.bigappleui.album.core.AlbumConfig;
import com.dazzle.bigappleui.album.entity.ImageItem;
import com.dazzle.bigappleui.album.theme.AlbumTheme;
import com.dazzle.bigappleui.album.theme.AlbumThemeUtils;
import com.dazzle.bigappleui.album.theme.DefaultAlbumTheme;
import com.dazzle.bigappleui.album.theme.custom.BlueAlbumTheme;
import com.dazzle.bigappleui.album.theme.custom.GreenAlbumTheme;
import com.winupon.andframe.bigapple.bitmap.BitmapDisplayConfig;
import com.winupon.andframe.bigapple.bitmap.local.LocalImageLoaderFace;
import com.winupon.andframe.bigapple.utils.AlertDialogUtils;

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

    private AlbumTheme albumTheme;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.demo_album);
        LocalImageLoaderFace.init(this);
        // 选择主题
        final Button theme = (Button) findViewById(R.id.theme);
        theme.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                AlertDialogUtils.displayAlert4SingleChoice2(AlbumDemo.this, "请选择不同主题", true, new String[] { "绿色", "蓝色",
                        "默认" }, new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface arg0, int position) {
                        switch (position) {
                        case 0:
                            albumTheme = new GreenAlbumTheme();
                            break;
                        case 1:
                            albumTheme = new BlueAlbumTheme();
                            break;
                        case 2:
                            albumTheme = new DefaultAlbumTheme();
                            break;
                        }

                        theme.setBackgroundColor(albumTheme.titleBgColor());
                    }
                });
            }
        });

        // 单选
        Button danxuan = (Button) findViewById(R.id.danxuan);
        danxuan.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                AlbumThemeUtils.setTheme(albumTheme);
                AlbumUtils.gotoAlbumForSingle(AlbumDemo.this, ACTIVITY_RESULT_1);
            }
        });

        // 多选
        Button duoxuan = (Button) findViewById(R.id.duoxuan);
        duoxuan.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                AlbumThemeUtils.setTheme(albumTheme);
                AlbumUtils.gotoAlbumForMulti(AlbumDemo.this, ACTIVITY_RESULT_2);
            }
        });

        // 多选有限制张数
        Button duoxuan2 = (Button) findViewById(R.id.duoxuan2);
        duoxuan2.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                AlbumThemeUtils.setTheme(albumTheme);
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
            List<ImageItem> temp1 = AlbumConfig.getSelListAndClear();
            for (ImageItem bi : temp1) {
                selPathList.add(bi.imagePath);
            }
            break;
        case ACTIVITY_RESULT_2:
            List<ImageItem> temp2 = AlbumConfig.getSelListAndClear();
            for (ImageItem bi : temp2) {
                selPathList.add(bi.imagePath);
            }
            break;
        case ACTIVITY_RESULT_3:
            List<ImageItem> temp3 = AlbumConfig.getSelListAndClear();
            for (ImageItem bi : temp3) {
                selPathList.add(bi.imagePath);
            }
            break;
        }
        adapter.notifyDataSetChanged();
    }

}
