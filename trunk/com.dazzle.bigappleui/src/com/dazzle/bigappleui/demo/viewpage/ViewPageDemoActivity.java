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
import com.dazzle.bigappleui.viewpage.ViewPage;
import com.dazzle.bigappleui.viewpage.event.OnScrollCompleteListener;
import com.dazzle.bigappleui.viewpage.event.ScrollEvent;

/**
 * 图片切换页demo
 * 
 * @author xuan
 */
public class ViewPageDemoActivity extends Activity {
    private ViewPage viewPage;
    private TextView textview;
    private Button button1;
    private Button button2;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.demo_viewpage_main);

        viewPage = (ViewPage) findViewById(R.id.viewPage);
        textview = (TextView) findViewById(R.id.textview);
        button1 = (Button) findViewById(R.id.button1);
        button2 = (Button) findViewById(R.id.button2);

        // 设置偏移量
        int offset = getIntent().getIntExtra("offset", 0);
        viewPage.setOffset(offset);

        // 设置滚动后的监听器
        viewPage.addOnScrollCompleteListener(new OnScrollCompleteListener() {
            @Override
            public void onScrollComplete(ScrollEvent scrollEvent) {
                textview.setText("我滑动到了屏幕：" + scrollEvent.curScreen);
            }
        });

        // 设置要切屏显示的view
        ImageView pic1 = (ImageView) LayoutInflater.from(this).inflate(R.layout.demo_viewpage_imagelayout, null);
        ImageView pic2 = (ImageView) LayoutInflater.from(this).inflate(R.layout.demo_viewpage_imagelayout, null);
        ImageView pic3 = (ImageView) LayoutInflater.from(this).inflate(R.layout.demo_viewpage_imagelayout, null);
        ImageView pic4 = (ImageView) LayoutInflater.from(this).inflate(R.layout.demo_viewpage_imagelayout, null);
        ImageView pic5 = (ImageView) LayoutInflater.from(this).inflate(R.layout.demo_viewpage_imagelayout, null);

        pic1.setImageResource(R.drawable.demo_viewpage_pic1);
        pic2.setImageResource(R.drawable.demo_viewpage_pic2);
        pic3.setImageResource(R.drawable.demo_viewpage_pic3);
        pic4.setImageResource(R.drawable.demo_viewpage_pic4);
        pic5.setImageResource(R.drawable.demo_viewpage_pic5);

        viewPage.addView(pic1);
        viewPage.addView(pic2);
        viewPage.addView(pic3);
        viewPage.addView(pic4);
        viewPage.addView(pic5);

        // 设置按钮，暴力跳转到指定界面
        button1.setText("跳转到第1个界面");
        button1.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewPage.setToScreen(0);
            }
        });

        // 设置偏移量
        if (offset == 0) {
            button2.setText("设置50px的偏移量");
            button2.setOnClickListener(new Button.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent();
                    intent.setClass(ViewPageDemoActivity.this, ViewPageDemoActivity.class);
                    intent.putExtra("offset", 50);
                    startActivity(intent);
                    ViewPageDemoActivity.this.finish();
                }
            });
        }
        else {
            button2.setText("取消偏移量");
            button2.setOnClickListener(new Button.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent();
                    intent.setClass(ViewPageDemoActivity.this, ViewPageDemoActivity.class);
                    startActivity(intent);
                    ViewPageDemoActivity.this.finish();
                }
            });
        }
    }

}
