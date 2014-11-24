/* 
 * @(#)DrawImageActivityDemo.java    Created on 2014-11-24
 * Copyright (c) 2014 ZDSoft Networks, Inc. All rights reserved.
 * $Id$
 */
package com.dazzle.bigappleui.demo;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import com.dazzle.bigappleui.view.img.ArrowImageView;
import com.dazzle.bigappleui.view.img.HookImageView;

/**
 * 测试自己绘制的一些图片
 * 
 * @author xuan
 * @version $Revision: 1.0 $, $Date: 2014-11-24 上午11:28:34 $
 */
public class DrawImageActivityDemo extends Activity {
    private LinearLayout root;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        root = new LinearLayout(this);
        root.setOrientation(LinearLayout.VERTICAL);
        setContentView(root);

        addView(new ArrowImageView(this));
        addView(new HookImageView(this));
    }

    private void addView(View view) {
        view.setLayoutParams(new LinearLayout.LayoutParams(100, 100));
        root.addView(view);
    }

}
