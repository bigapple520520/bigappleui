/* 
 * @(#)GragGridViewDemo.java    Created on 2015-1-20
 * Copyright (c) 2015 ZDSoft Networks, Inc. All rights reserved.
 * $Id$
 */
package com.dazzle.bigappleui.demo.view.gridview;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.dazzle.bigappleui.R;
import com.dazzle.bigappleui.view.gridview.DragGridView;

/**
 * GragGridView的demo测试
 * 
 * @author xuan
 * @version $Revision: 1.0 $, $Date: 2015-1-20 下午4:33:24 $
 */
public class DragGridViewDemo extends Activity {

    private DragGridView dragGridView;
    private int[] resids;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        resids = new int[] { R.drawable.pic1, R.drawable.pic2, R.drawable.pic3, R.drawable.pic4,
                R.drawable.demo_gifview_pic, R.drawable.demo_roundedimageview_pic, R.drawable.ic_action_search };

        dragGridView = new DragGridView(this);
        dragGridView.setNumColumns(3);
        dragGridView.setAdapter(new BaseAdapter() {
            @Override
            public int getCount() {
                return resids.length;
            }

            @Override
            public View getView(int position, View arg1, ViewGroup arg2) {
                ImageView image = new ImageView(DragGridViewDemo.this);
                image.setImageResource(resids[position]);
                return image;
            }

            @Override
            public Object getItem(int arg0) {
                return null;
            }

            @Override
            public long getItemId(int arg0) {
                return 0;
            }
        });
        setContentView(dragGridView);
    }

}
