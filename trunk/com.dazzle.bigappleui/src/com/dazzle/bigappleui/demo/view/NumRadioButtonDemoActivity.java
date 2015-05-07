/* 
 * @(#)NumRadioButtonDemoActivity.java    Created on 2014-9-12
 * Copyright (c) 2014 ZDSoft Networks, Inc. All rights reserved.
 * $Id$
 */
package com.dazzle.bigappleui.demo.view;

import android.app.Activity;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;

import com.dazzle.bigappleui.R;
import com.dazzle.bigappleui.utils.UnreadBitmapProcessor;
import com.dazzle.bigappleui.view.NumRadioButton;
import com.winupon.andframe.bigapple.utils.ToastUtils;

/**
 * RadioButton加未读消息
 * 
 * @author xuan
 * @version $Revision: 1.0 $, $Date: 2014-9-12 上午9:35:39 $
 */
public class NumRadioButtonDemoActivity extends Activity {
    private RadioGroup tabs;
    private NumRadioButton tabbtn0;
    private NumRadioButton tabbtn1;
    private NumRadioButton tabbtn2;
    private NumRadioButton tabbtn3;
    private NumRadioButton tabbtn4;

    private Button button;
    private ImageView image;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.demo_numradiobutton_main);
        tabbtn0 = (NumRadioButton) findViewById(R.id.tabbtn0);
        tabbtn1 = (NumRadioButton) findViewById(R.id.tabbtn1);
        tabbtn2 = (NumRadioButton) findViewById(R.id.tabbtn2);
        tabbtn3 = (NumRadioButton) findViewById(R.id.tabbtn3);
        tabbtn4 = (NumRadioButton) findViewById(R.id.tabbtn4);
        tabs = (RadioGroup) findViewById(R.id.tabs);
        button = (Button) findViewById(R.id.button);
        image = (ImageView) findViewById(R.id.image);

        final UnreadBitmapProcessor processor = new UnreadBitmapProcessor(this, R.drawable.msg_new);
        processor.setOffsetHeightByDp(50);
        processor.setOffsetWidthByDp(50);

        // tabbtn0.setNum(4, R.drawable.msg_new);
        tabbtn0.setPoint(R.drawable.msg_new);
        tabbtn0.clearNum();

        tabbtn1.setOffsetHeightByDp(10);
        tabbtn1.setOffsetWidthByDp(10);
        tabbtn1.setNum(3, R.drawable.msg_new);

        tabbtn2.setNum(2, R.drawable.msg_new);

        tabbtn3.setNum(9, NumRadioButton.TRANSPARENT_BITMAP);
        tabbtn3.setPaintColor(Color.BLACK);

        tabbtn4.setNum(99, R.drawable.msg_new);

        button.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                image.setImageBitmap(processor.process(BitmapFactory.decodeResource(getResources(), R.drawable.pic1), 1));
            }
        });

        tabs.setOnCheckedChangeListener(new OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup arg0, int position) {
                ToastUtils.displayTextShort(NumRadioButtonDemoActivity.this, "选中" + position);
            }
        });
    }
}
