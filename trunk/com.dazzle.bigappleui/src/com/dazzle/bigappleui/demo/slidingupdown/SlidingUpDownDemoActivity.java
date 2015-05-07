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
import com.dazzle.bigappleui.slidingupdown.SlidingUpDown.OpenPercentListener;

/**
 * 上拉下拉控件
 * 
 * @author xuan
 * @version $Revision: 1.0 $, $Date: 2014-1-17 下午5:22:39 $
 */
public class SlidingUpDownDemoActivity extends Activity {
    private SlidingUpDown slidingUpDown;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.demo_slidingupdown_main);

        // 侧滑主界面
        View aboveView = LayoutInflater.from(this).inflate(R.layout.demo_slidingupdown_above, null);

        // 侧滑菜单左边的界面
        final View behindView = LayoutInflater.from(this).inflate(R.layout.demo_slidingupdown_behind, null);

        // 设置侧滑界面参数
        slidingUpDown = (SlidingUpDown) findViewById(R.id.slidingUpDown);
        slidingUpDown.setAboveContent(aboveView);
        slidingUpDown.setBehindContent(behindView);

        // 参数配置
        slidingUpDown.setMode(SlidingUpDown.MODE_UP_DOWN);

        slidingUpDown.setFadeEnabled(true);
        slidingUpDown.setFadeDegree(0.99f);

        // 设置打开的渐变
        slidingUpDown.setOpenPercentListener(new OpenPercentListener() {
            @Override
            public void openPercent(float percent) {
                int padding = (int) (40 * Math.abs(1 - percent)) + 5;
                behindView.setPadding(padding, padding, padding, padding);
            }
        });
    }
}
