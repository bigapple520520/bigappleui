package com.example.bigappleui_demo.viewpager;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.Toast;

import com.dazzle.bigappleui.viewpager.ViewPagerLayout;
import com.dazzle.bigappleui.viewpager.event.OnScrollCompleteListener;
import com.dazzle.bigappleui.viewpager.event.ScrollEvent;
import com.example.bigappleui_demo.R;

/**
 * ViewPagerLayout控件的demo测试
 * 
 * @author xuan
 * @version $Revision: 1.0 $, $Date: 2013-9-26 下午5:14:59 $
 */
public class Main extends Activity {

    private ViewPagerLayout viewPagerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.viewpager_main);

        viewPagerLayout = (ViewPagerLayout) findViewById(R.id.viewPagerLayout);

        // 设置偏移量
        viewPagerLayout.setOffsetSize(50);

        // 设置滚动后的监听器
        viewPagerLayout.setOnScrollCompleteLinstenner(new OnScrollCompleteListener() {
            @Override
            public void onScrollComplete(ScrollEvent scrollEvent) {
                Toast.makeText(Main.this, "我滑动到了屏幕：" + scrollEvent.curScreen, Toast.LENGTH_SHORT).show();
            }
        });

        // 设置要切屏显示的view
        ImageView pic1 = (ImageView) LayoutInflater.from(this).inflate(R.layout.viewpager_image_layout, null);
        pic1.setImageResource(R.drawable.pic1);

        ImageView pic2 = (ImageView) LayoutInflater.from(this).inflate(R.layout.viewpager_image_layout, null);
        pic2.setImageResource(R.drawable.pic2);

        ImageView pic3 = (ImageView) LayoutInflater.from(this).inflate(R.layout.viewpager_image_layout, null);
        pic3.setImageResource(R.drawable.pic3);

        ImageView pic4 = (ImageView) LayoutInflater.from(this).inflate(R.layout.viewpager_image_layout, null);
        pic4.setImageResource(R.drawable.pic4);

        viewPagerLayout.addView(pic1);
        viewPagerLayout.addView(pic2);
        viewPagerLayout.addView(pic3);
        viewPagerLayout.addView(pic4);
    }

}
