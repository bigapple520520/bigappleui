package com.example.bigappleui_demo.view.roundedimageview;

import android.app.Activity;
import android.os.Bundle;

import com.dazzle.bigappleui.view.RoundedImageView;
import com.example.bigappleui_demo.R;

/**
 * 设置圆角图片控件
 * 
 * @author xuan
 * @version $Revision: 1.0 $, $Date: 2013-9-28 下午3:57:15 $
 */
public class Main extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.demo_view_roundedimageview);

        RoundedImageView roundedImageView = (RoundedImageView) findViewById(R.id.roundedImageView);

        // roundedImageView.setRoundPx(500f);// 自定义设置roundPx，默认值20
        roundedImageView.setImageResource(R.drawable.pic1);
    }
}
