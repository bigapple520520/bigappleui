package com.dazzle.bigappleui.demo.camera;

import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.dazzle.bigappleui.R;
import com.dazzle.bigappleui.camera.BPUICameraUtils;
import com.winupon.andframe.bigapple.ioc.InjectView;
import com.winupon.andframe.bigapple.ioc.app.AnActivity;
import com.winupon.andframe.bigapple.utils.ToastUtils;

/**
 * 相机使用DEMO
 * 
 * @author xuan
 * 
 */
public class CameraDemo extends AnActivity {
	public static final int CAMERA_CODE = 1;

	@InjectView(R.id.button)
	private Button button;

	@InjectView(R.id.imageView)
	private ImageView imageView;

	private String dir = Environment.getExternalStorageDirectory().getPath()
			+ "/bigappleuidemo/camera/";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.demo_camera);

		button.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				BPUICameraUtils.openCamera(CameraDemo.this, CAMERA_CODE, dir
						+ "temp.jpg");
			}
		});
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode != RESULT_OK) {
			ToastUtils.displayTextShort(this, "拍照取消");
			return;
		}

		switch (requestCode) {
		case CAMERA_CODE:
			imageView
					.setImageBitmap(BitmapFactory.decodeFile(dir + "temp.jpg"));
			break;
		}

	}
}
