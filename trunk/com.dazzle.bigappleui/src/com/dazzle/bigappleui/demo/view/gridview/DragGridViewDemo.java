/* 
 * @(#)GragGridViewDemo.java    Created on 2015-1-20
 * Copyright (c) 2015 ZDSoft Networks, Inc. All rights reserved.
 * $Id$
 */
package com.dazzle.bigappleui.demo.view.gridview;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.dazzle.bigappleui.R;
import com.dazzle.bigappleui.view.gridview.DragGridView;
import com.dazzle.bigappleui.view.gridview.DragGridView.OnChanageListener;

/**
 * GragGridView的demo测试
 * 
 * @author xuan
 * @version $Revision: 1.0 $, $Date: 2015-1-20 下午4:33:24 $
 */
public class DragGridViewDemo extends Activity {

    private DragGridView dragGridView;
    private List<Integer> resids = new ArrayList<Integer>();
    private BaseAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        resids.add(R.drawable.pic1);
        resids.add(R.drawable.pic2);
        resids.add(R.drawable.pic3);
        resids.add(R.drawable.pic4);
        resids.add(R.drawable.demo_gifview_pic);
        resids.add(R.drawable.demo_roundedimageview_pic);
        resids.add(R.drawable.ic_action_search);

        dragGridView = new DragGridView(this);
        dragGridView.setNumColumns(3);
        adapter = new BaseAdapter() {
            @Override
            public int getCount() {
                return resids.size();
            }

            @Override
            public View getView(int position, View arg1, ViewGroup arg2) {
                View view = LayoutInflater.from(DragGridViewDemo.this).inflate(R.layout.demo_image, null);
                ImageView image = (ImageView) view.findViewById(R.id.image);
                image.setBackgroundResource(resids.get(position));
                return view;
            }

            @Override
            public Object getItem(int arg0) {
                return null;
            }

            @Override
            public long getItemId(int arg0) {
                return 0;
            }
        };
        dragGridView.setAdapter(adapter);

        dragGridView.setOnChangeListener(new OnChanageListener() {
            @Override
            public void onChange(int form, int to) {
                // int min = Math.min(form, to);
                // int max = Math.min(form, to);
                //
                // int minRes = resids.remove(min);
                // resids.add(min, resids.get(max));
                //
                // resids.remove(min);
                // resids.add(max, minRes);
                // adapter.notifyDataSetChanged();

                if (form > to) {
                    int temp = resids.remove(form);
                    resids.add(to, temp);
                }
                else {
                    int temp = resids.remove(form);
                    resids.add(to, temp);
                }
                adapter.notifyDataSetChanged();
                Log.d("ttttttttttttttttt", "from:" + form + "to:" + to);
            }
        });

        setContentView(dragGridView);
    }
}
