/* 
 * @(#)DrawImageActivityDemo.java    Created on 2014-11-24
 * Copyright (c) 2014 ZDSoft Networks, Inc. All rights reserved.
 * $Id$
 */
package com.dazzle.bigappleui.demo;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.dazzle.bigappleui.utils.ui.drawable.ArrowDrawable;
import com.dazzle.bigappleui.utils.ui.drawable.HookDrawable;
import com.dazzle.bigappleui.utils.ui.drawable.checkbox.CheckBoxNormalDrawable;
import com.dazzle.bigappleui.utils.ui.drawable.checkbox.CheckBoxSelectedDrawable;
import com.dazzle.bigappleui.utils.ui.drawable.fileicon.DefaultFileDrawable;
import com.dazzle.bigappleui.utils.ui.drawable.fileicon.FolderDrawable;
import com.dazzle.bigappleui.utils.ui.drawable.fileicon.TxtFileDrawable;
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

        ImageView i = new ImageView(this);
        i.setImageDrawable(new ArrowDrawable());
        addView(i);

        ImageView i2 = new ImageView(this);
        i2.setImageDrawable(new HookDrawable());
        addView(i2);

        ImageView i3 = new ImageView(this);
        i3.setImageDrawable(new FolderDrawable());
        addView(i3);

        ImageView i4 = new ImageView(this);
        i4.setImageDrawable(new DefaultFileDrawable());
        addView(i4);

        ImageView i5 = new ImageView(this);
        i5.setImageDrawable(new CheckBoxNormalDrawable());
        addView(i5);

        ImageView i6 = new ImageView(this);
        i6.setImageDrawable(new CheckBoxSelectedDrawable());
        addView(i6);

        ImageView i7 = new ImageView(this);
        i7.setImageDrawable(new TxtFileDrawable());
        addView(i7);
    }

    private void addView(View view) {
        view.setLayoutParams(new LinearLayout.LayoutParams(80, 80));
        root.addView(view);
    }

}
