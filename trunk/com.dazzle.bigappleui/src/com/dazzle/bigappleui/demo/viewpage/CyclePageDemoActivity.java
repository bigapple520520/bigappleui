package com.dazzle.bigappleui.demo.viewpage;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.dazzle.bigappleui.R;
import com.dazzle.bigappleui.viewpage.CyclePage;
import com.dazzle.bigappleui.viewpage.event.OnScrollCompleteListener;
import com.dazzle.bigappleui.viewpage.event.ScrollEvent;

/**
 * 图片切换页demo
 * 
 * @author xuan
 */
public class CyclePageDemoActivity extends Activity {
    private CyclePage cyclePage;
    private TextView textview;
    private Button button1;
    private Button button2;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.demo_cyclepage_main);

        cyclePage = (CyclePage) findViewById(R.id.cyclePage);
        textview = (TextView) findViewById(R.id.textview);
        button1 = (Button) findViewById(R.id.button1);
        button2 = (Button) findViewById(R.id.button2);

        // 设置偏移量
        int offset = getIntent().getIntExtra("offset", 0);
        cyclePage.setOffset(offset);

        // 设置滚动后的监听器
        cyclePage.addOnScrollCompleteListener(new OnScrollCompleteListener() {
            @Override
            public void onScrollComplete(ScrollEvent scrollEvent) {
                if (scrollEvent.curScreen == 0) {
                    cyclePage.setToScreen(5);
                }

                if (scrollEvent.curScreen == 6) {
                    cyclePage.setToScreen(1);
                }

                textview.setText("我滑动到了屏幕：" + scrollEvent.curScreen);
            }
        });

        // 设置要切屏显示的view
        ImageView pic55 = (ImageView) LayoutInflater.from(this).inflate(R.layout.demo_viewpage_imagelayout, null);
        ImageView pic1 = (ImageView) LayoutInflater.from(this).inflate(R.layout.demo_viewpage_imagelayout, null);
        ImageView pic2 = (ImageView) LayoutInflater.from(this).inflate(R.layout.demo_viewpage_imagelayout, null);
        ImageView pic3 = (ImageView) LayoutInflater.from(this).inflate(R.layout.demo_viewpage_imagelayout, null);
        ImageView pic4 = (ImageView) LayoutInflater.from(this).inflate(R.layout.demo_viewpage_imagelayout, null);
        ImageView pic5 = (ImageView) LayoutInflater.from(this).inflate(R.layout.demo_viewpage_imagelayout, null);
        ImageView pic11 = (ImageView) LayoutInflater.from(this).inflate(R.layout.demo_viewpage_imagelayout, null);

        pic55.setImageResource(R.drawable.demo_viewpage_pic5);
        pic1.setImageResource(R.drawable.demo_viewpage_pic1);
        pic2.setImageResource(R.drawable.demo_viewpage_pic2);
        pic3.setImageResource(R.drawable.demo_viewpage_pic3);
        pic4.setImageResource(R.drawable.demo_viewpage_pic4);
        pic5.setImageResource(R.drawable.demo_viewpage_pic5);
        pic11.setImageResource(R.drawable.demo_viewpage_pic1);

        cyclePage.addView(pic55);
        cyclePage.addView(pic1);
        cyclePage.addView(pic2);
        cyclePage.addView(pic3);
        cyclePage.addView(pic4);
        cyclePage.addView(pic5);
        cyclePage.addView(pic11);

        // 设置按钮，暴力跳转到指定界面
        button1.setText("暂停");
        button1.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (cyclePage.isPause()) {
                    cyclePage.setPause(false);
                    button1.setText("暂停");
                }
                else {
                    cyclePage.setPause(true);
                    button1.setText("启动");
                }
            }
        });

        // 设置偏移量
        if (offset == 0) {
            button2.setText("设置50px的偏移量");
            button2.setOnClickListener(new Button.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent();
                    intent.setClass(CyclePageDemoActivity.this, CyclePageDemoActivity.class);
                    intent.putExtra("offset", 50);
                    startActivity(intent);
                    CyclePageDemoActivity.this.finish();
                }
            });
        }
        else {
            button2.setText("取消偏移量");
            button2.setOnClickListener(new Button.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent();
                    intent.setClass(CyclePageDemoActivity.this, CyclePageDemoActivity.class);
                    startActivity(intent);
                    CyclePageDemoActivity.this.finish();
                }
            });
        }
    }

}
