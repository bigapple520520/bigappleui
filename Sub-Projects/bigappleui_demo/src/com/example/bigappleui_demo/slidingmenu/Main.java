/* 
 * @(#)DemoSlidingActivity.java    Created on 2013-11-6
 * Copyright (c) 2013 ZDSoft Networks, Inc. All rights reserved.
 * $Id$
 */
package com.example.bigappleui_demo.slidingmenu;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.dazzle.bigappleui.slidingmenu.SlidingMenu;
import com.dazzle.bigappleui.slidingmenu.app.SlidingActivity;
import com.example.bigappleui_demo.R;

public class Main extends SlidingActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.demo_content);
        setBehindContentView(R.layout.demo_behind);
        getSlidingMenu().setSecondaryMenu(R.layout.demo_behind);
        getSlidingMenu().setMode(SlidingMenu.LEFT_RIGHT);
        getSlidingMenu().setBehindOffset(50);

        Button button = (Button) findViewById(R.id.button);
        button.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(
                        Main.this,
                        getSlidingMenu().getmViewBehind().getLeft() + "---"
                                + getSlidingMenu().getmViewBehind().getRight(), Toast.LENGTH_LONG).show();
            }
        });

        Button button2 = (Button) findViewById(R.id.button2);
        button2.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(
                        Main.this,
                        getSlidingMenu().getmViewAbove().getLeft() + "---"
                                + getSlidingMenu().getmViewAbove().getRight(), Toast.LENGTH_LONG).show();
            }
        });
    }

}
