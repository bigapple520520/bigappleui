package com.dazzle.bigappleui.demo.view.zoomimageview;

import android.app.Activity;
import android.os.Bundle;

import com.dazzle.bigappleui.R;
import com.dazzle.bigappleui.view.ZoomImageView;

/**
 * 图片显示，支持两点缩放，及旋转
 * 
 * @author xuan
 */
public class ZoomImageViewDemoActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.demo_zoomimageview_main);

		ZoomImageView zoomImageView = (ZoomImageView) findViewById(R.id.zoomImageView);
		zoomImageView.setImageResource(R.drawable.demo_zoomimageview_pic);// 也可以直接在布局文件的src属性中设置
	}

}
