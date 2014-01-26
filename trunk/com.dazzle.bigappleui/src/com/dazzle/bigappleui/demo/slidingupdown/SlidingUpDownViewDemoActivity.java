/* 
 * @(#)SlidingUpDownDemoActivity.java    Created on 2014-1-17
 * Copyright (c) 2014 ZDSoft Networks, Inc. All rights reserved.
 * $Id$
 */
package com.dazzle.bigappleui.demo.slidingupdown;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

import com.dazzle.bigappleui.R;
import com.dazzle.bigappleui.slidingupdown.SlidingUpDown;

/**
 * 上拉下拉控件
 * 
 * @author xuan
 * @version $Revision: 1.0 $, $Date: 2014-1-17 下午5:22:39 $
 */
public class SlidingUpDownViewDemoActivity extends Activity {
    private SlidingUpDown slidingUpDown;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.demo_slidingupdown_main);

        // 侧滑主界面
        View above = LayoutInflater.from(this).inflate(R.layout.demo_slidingmenu_above, null);

        // 侧滑菜单左边的界面
        View menuDown = LayoutInflater.from(this).inflate(R.layout.demo_slidingmenu_menu_right, null);

        // 设置侧滑界面参数
        slidingUpDown = (SlidingUpDown) findViewById(R.id.slidingUpDown);
        slidingUpDown.setAboveContent(above);
        slidingUpDown.setBehindContent(menuDown);
        slidingUpDown.setMode(SlidingUpDown.MODE_UP_DOWN);
    }

}
