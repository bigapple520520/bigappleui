package com.dazzle.bigappleui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;

import com.dazzle.bigappleui.demo.gifview.GifViewDemoActivity;
import com.dazzle.bigappleui.demo.lettersort.LetterSortDemoActivity;
import com.dazzle.bigappleui.demo.pull2refresh.Pull2RefreshDemoActivity;
import com.dazzle.bigappleui.demo.slidingmenu.SlidingMenuDemoActivity;
import com.dazzle.bigappleui.demo.slidingupdown.SlidingUpDownDemoActivity;
import com.dazzle.bigappleui.demo.view.SlipButtonDemoActivity;
import com.dazzle.bigappleui.demo.view.roundedimageview.RoundedImageViewDemoActivity;
import com.dazzle.bigappleui.demo.view.zoomimageview.ZoomImageViewDemoActivity;
import com.dazzle.bigappleui.demo.viewpage.CyclePageDemoActivity;
import com.dazzle.bigappleui.demo.viewpage.ViewPageDemoActivity;

public class BigappleUiMain extends Activity {
    private LinearLayout content;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        content = (LinearLayout) findViewById(R.id.content);

        addButton("gif动态图显示", GifViewDemoActivity.class);
        addButton("viewpage图片切页实现", ViewPageDemoActivity.class);
        addButton("cyclepage图片切页实现", CyclePageDemoActivity.class);
        addButton("列表字母分类排序 + 侧滑删除", LetterSortDemoActivity.class);
        addButton("侧滑模式实现", SlidingMenuDemoActivity.class);
        addButton("上下滑动有惊喜实现", SlidingUpDownDemoActivity.class);
        addButton("下拉刷新实现", Pull2RefreshDemoActivity.class);
        addButton("图片显示支持缩放旋转实现", ZoomImageViewDemoActivity.class);
        addButton("圆角图片实现", RoundedImageViewDemoActivity.class);
        addButton("SlipButton滑块demo", SlipButtonDemoActivity.class);
    }

    private void addButton(String text, final Class<?> clazz) {
        Button button = new Button(this);
        button.setText(text);
        button.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setClass(BigappleUiMain.this, clazz);
                startActivity(intent);
            }
        });
        content.addView(button);
    }

}
