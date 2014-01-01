package com.dazzle.bigappleui.demo.view.roundedimageview;

import android.app.Activity;
import android.os.Bundle;

import com.dazzle.bigappleui.R;
import com.dazzle.bigappleui.view.RoundedImageView;

/**
 * 设置圆角图片控件
 * 
 * @author xuan
 * @version $Revision: 1.0 $, $Date: 2013-9-28 下午3:57:15 $
 */
public class RoundedImageViewDemoActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.demo_roundedimageview_main);

		RoundedImageView roundedImageView = (RoundedImageView) findViewById(R.id.roundedImageView);
		roundedImageView.setRoundPx(20f);
		roundedImageView.setImageResource(R.drawable.demo_roundedimageview_pic);
	}

}
