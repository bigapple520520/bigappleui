package com.example.bigappleui_demo.zoomimageview;

import android.app.Activity;
import android.os.Bundle;

import com.dazzle.bigappleui.zoomimageview.ZoomImageView;
import com.example.bigappleui_demo.R;

public class Main extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.demo_zoomiamgeview);

        ZoomImageView zoomImageView = (ZoomImageView) findViewById(R.id.zoomImageView);
        zoomImageView.setImageResource(R.drawable.pic1);
    }
}
