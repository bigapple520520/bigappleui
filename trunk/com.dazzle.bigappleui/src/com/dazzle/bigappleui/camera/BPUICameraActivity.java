package com.dazzle.bigappleui.camera;

import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.hardware.Camera;
import android.hardware.Camera.PictureCallback;
import android.hardware.Camera.ShutterCallback;
import android.os.Bundle;
import android.os.Handler;
import android.os.Vibrator;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.dazzle.bigappleui.utils.ui.M;
import com.winupon.andframe.bigapple.ioc.app.AnActivity;
import com.winupon.andframe.bigapple.utils.Validators;
import com.winupon.andframe.bigapple.utils.log.LogUtils;
import com.winupon.andframe.bigapple.utils.sharepreference.PreferenceModel;

/**
 * 拍照界面，使用说明<br>
 * 
 * 1、拷贝下列资源文件到自己的项目中去<br>
 * res/drawable/bpui_camera_btn_take_picture.xml<br>
 * res/drawable/bpui_camera_progress.xml<br>
 * 
 * res/drawable-xhdpi/bpui_camera_btn_take_picture_normal.png<br>
 * res/drawable-xhdpi/bpui_camera_btn_take_picture_pressed.png<br>
 * res/drawable-xhdpi/bpui_camera_flash_close.png<br>
 * res/drawable-xhdpi/bpui_camera_flash_open.png<br>
 * res/drawable-xhdpi/bpui_camera_progress_ball.png<br>
 * res/drawable-xhdpi/bpui_camera_progress_volumn_bg.9.png<br>
 * res/drawable-xhdpi/bpui_camera_progress_volumn_front.9.png<br>
 * res/drawable-xhdpi/bpui_camera_progress_volumn_primary.9.png<br>
 * 
 * res/layout/bpui_camera.xml<br>
 * 
 * 2、在AndroidManifest.xml中配置拍照界面和拍照所需权限<br>
 * 界面:<br>
 * <activity android:name="com.dazzle.bigappleui.camera.BPUICameraActivity"
 * android:theme="@android:style/Theme.Black.NoTitleBar"/><br>
 * 权限：<br>
 * <uses-permission android:name="android.permission.CAMERA" /><br>
 * <uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE" /><br>
 * <uses-permission android:name="android.permission.VIBRATE" /><br>
 * <uses-permission android:name="android.permission.FLASHLIGHT" /><br>
 * 
 * 3、然后代码调用：<br>
 * BPUICameraUtils.openCamera(context, REQUEST_CODE, outputPath);<br>
 * 
 * @author xuan
 */
public class BPUICameraActivity extends AnActivity {
	private static final String TAG = "BPUICameraActivity";
	public static final String IMAGET_OUTPUT_PATH = "image.output.path";

	/** 相机预览 */
	private BPUICameraPreview cameraPreview;
	/** 拍照按钮 */
	private Button takePhotoBtn;
	/** 闪光灯 */
	private ImageView flashBtn;

	/** 控制条布局 */
	private View seekBarLayout;
	/** 焦距 */
	private SeekBar jiaojuSeekBar;
	/** 曝光 */
	private SeekBar baoguangSeekBar;

	/** 点击View */
	private View clickView;
	/** 相机对象 */
	private Camera camera;
	/** 相机参数对象 */
	private Camera.Parameters parameters = null;

	/** 底部布局 */
	private View tabLayout;
	/** 取消 */
	private TextView cancelTv;

	/** 底部布局 2 */
	private View tabLayout2;
	/** 重拍 */
	private TextView resetTv;
	/** 使用照片 */
	private TextView okTv;

	/** 拍照后查看大图 */
	private ImageView finishShowIv;

	private int maxZoom;// 最大缩放比例

	private int maxExposure;// 最大曝光度：如果是0，表示不支持调节曝光度
	private int minExposure;// 最小曝光度：如果是0，表示不支持调节曝光度
	private int intervalExposure;// 最小曝光度：最大-最小

	/** 防止多次保存 */
	private boolean isSaving = false;

	private Handler handler = new Handler();
	private Runnable hideSeekBarRunnable;

	/** 用户传递过来拍完照存放的位置 */
	private String imageSavePath;

	/** 判断是否处于显示图片状态 */
	private boolean isShowImage = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(M.layout(this, "bpui_camera"));
		imageSavePath = getIntent().getStringExtra(IMAGET_OUTPUT_PATH);

		findView();
		initWidgets();
	}

	private void findView() {
		cameraPreview = (BPUICameraPreview) findViewById(M.id(this,
				"cameraPreview"));
		takePhotoBtn = (Button) findViewById(M.id(this, "takePhotoBtn"));
		flashBtn = (ImageView) findViewById(M.id(this, "flashBtn"));

		seekBarLayout = findViewById(M.id(this, "seekBarLayout"));
		jiaojuSeekBar = (SeekBar) findViewById(M.id(this, "jiaojuSeekBar"));
		baoguangSeekBar = (SeekBar) findViewById(M.id(this, "baoguangSeekBar"));

		clickView = findViewById(M.id(this, "clickView"));

		tabLayout = findViewById(M.id(this, "tabLayout"));
		cancelTv = (TextView) findViewById(M.id(this, "cancelTv"));

		tabLayout2 = findViewById(M.id(this, "tabLayout2"));
		resetTv = (TextView) findViewById(M.id(this, "resetTv"));
		okTv = (TextView) findViewById(M.id(this, "okTv"));

		finishShowIv = (ImageView) findViewById(M.id(this, "finishShowIv"));
	}

	private void initWidgets() {
		// 相机预览
		cameraPreview.setInitRunnable(new Runnable() {
			@Override
			public void run() {
				camera = cameraPreview.getCamera();
				parameters = camera.getParameters();
				// 初始化初始焦距
				if (parameters.isZoomSupported()) {
					maxZoom = parameters.getMaxZoom();
					parameters.setZoom(jiaojuSeekBar.getProgress() * maxZoom
							/ 100);
				}

				// 曝光度
				maxExposure = parameters.getMaxExposureCompensation();
				minExposure = parameters.getMinExposureCompensation();
				intervalExposure = maxExposure - minExposure;
				if (0 != intervalExposure) {
					parameters.setExposureCompensation((baoguangSeekBar
							.getProgress() * intervalExposure / 100)
							- Math.abs(minExposure));
				}

				// 设置参数
				camera.setParameters(parameters);
			}
		});

		// 触摸显示控制条
		clickView.setOnTouchListener(new View.OnTouchListener() {
			@Override
			public boolean onTouch(View view, MotionEvent event) {
				// 显示控制条，并记录最后点击坐标
				showSeekBar();
				return false;
			}
		});

		// 焦距控制条
		jiaojuSeekBar.setOnTouchListener(new View.OnTouchListener() {
			@Override
			public boolean onTouch(View view, MotionEvent event) {
				showSeekBar();
				return false;
			}
		});
		// 点击出现聚焦
		clickView.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				if (null != camera) {
					camera.autoFocus(null);
				}
			}
		});

		// 点击拍照
		takePhotoBtn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				if (isSaving) {
					return;
				}

				isSaving = true;
				if (null != camera) {
					takePicture(null, null, new MPictureCallback());
				}
			}
		});

		// 闪光灯按钮
		refreshFlashBtn();
		flashBtn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				boolean isOpen = PreferenceModel.instance(
						BPUICameraActivity.this).getBoolean(
						BPUICameraConfig.CAMERA_FLASH_OPEN, false);
				PreferenceModel.instance(BPUICameraActivity.this).putBoolean(
						BPUICameraConfig.CAMERA_FLASH_OPEN, !isOpen);
				refreshFlashBtn();
			}
		});

		// 调节焦距
		jiaojuSeekBar
				.setOnSeekBarChangeListener(new BPUIOnSeekBarChangeListener() {
					@Override
					public void onProgressChanged(SeekBar vBar, int progress,
							boolean fromUser) {
						try {
							BPUICameraUtils.debug(TAG, "-----------progress:"
									+ progress);
							parameters.setZoom(progress * maxZoom / 100);
							camera.setParameters(parameters);
						} catch (Exception e) {
							LogUtils.e(e.getMessage(), e);
						}
					}
				});

		// 曝光调节
		baoguangSeekBar
				.setOnSeekBarChangeListener(new BPUIOnSeekBarChangeListener() {
					@Override
					public void onProgressChanged(SeekBar vBar, int progress,
							boolean fromUser) {
						try {
							BPUICameraUtils.debug(TAG, "-----------progress:"
									+ progress);
							parameters.setExposureCompensation((progress
									* intervalExposure / 100)
									- Math.abs(minExposure));
							camera.setParameters(parameters);
						} catch (Exception e) {
							LogUtils.e(e.getMessage(), e);
						}
					}
				});

		// 隐藏seekBar任务类
		hideSeekBarRunnable = new Runnable() {
			@Override
			public void run() {
				seekBarLayout.setVisibility(View.GONE);
			}
		};

		// 取消
		cancelTv.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				setResult(RESULT_CANCELED, getIntent());
				BPUICameraActivity.this.finish();
			}
		});

		// 重拍
		resetTv.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				isShowImage = false;
				changeIsShowImage();
			}
		});

		// 使用照片
		okTv.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				Intent intent = getIntent();
				intent.putExtra(BPUICameraActivity.IMAGET_OUTPUT_PATH,
						imageSavePath);
				setResult(RESULT_OK, getIntent());
				BPUICameraActivity.this.finish();
			}
		});
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		switch (keyCode) {
		case KeyEvent.KEYCODE_CAMERA: // 按下拍照按钮
			if (null != camera && event.getRepeatCount() == 0) {
				// 拍照
				// 注：调用takePicture()方法进行拍照是传入了一个PictureCallback对象——当程序获取了拍照所得的图片数据之后
				// ，PictureCallback对象将会被回调，该对象可以负责对相片进行保存或传入网络
				takePicture(null, null, new MPictureCallback());
			}
		}
		return super.onKeyDown(keyCode, event);
	}

	// 显示调控焦距的控件
	private void showSeekBar() {
		if (!isShowImage) {
			seekBarLayout.setVisibility(View.VISIBLE);
			handler.removeCallbacks(hideSeekBarRunnable);
			handler.postDelayed(hideSeekBarRunnable, 3000);
		}
	}

	// 按下拍照
	private void takePicture(ShutterCallback shutter, PictureCallback raw,
			PictureCallback jpeg) {
		boolean isFlashOpen = PreferenceModel.instance(BPUICameraActivity.this)
				.getBoolean(BPUICameraConfig.CAMERA_FLASH_OPEN, false);
		if (isFlashOpen) {
			// 拍照时，开启闪光灯
			Camera.Parameters parameters = camera.getParameters();
			parameters.setFlashMode(Camera.Parameters.FLASH_MODE_ON);
			camera.setParameters(parameters);
		} else {
			Camera.Parameters parameters = camera.getParameters();
			parameters.setFlashMode(Camera.Parameters.FLASH_MODE_OFF);
			camera.setParameters(parameters);
		}

		camera.takePicture(shutter, raw, jpeg);
	}

	/**
	 * 按下快门回调
	 * 
	 * @author xuan
	 */
	private final class MPictureCallback implements PictureCallback {
		@Override
		public void onPictureTaken(final byte[] data, final Camera camera) {
			// 震动一下手感
			Vibrator vibrator = (Vibrator) BPUICameraActivity.this
					.getSystemService(Context.VIBRATOR_SERVICE);
			vibrator.vibrate(30);

			try {
				imageSavePath = BPUICameraUtils.saveImage(data, imageSavePath,
						cameraPreview.getDisplayDegree());
				if (!Validators.isEmpty(imageSavePath)) {
					// 显示大图
					finishShowIv.setImageBitmap(BitmapFactory
							.decodeFile(imageSavePath));
					isShowImage = true;
					changeIsShowImage();
				} else {
					// 保存失败，重新开始预览拍照
					isShowImage = false;
					changeIsShowImage();
				}
				isSaving = false;
			} catch (Exception e) {
				LogUtils.e(e.getMessage(), e);
			}
		}
	}

	/** 切换拍照还是显示图片状态 */
	private void changeIsShowImage() {
		if (isShowImage) {
			flashBtn.setVisibility(View.GONE);
			tabLayout.setVisibility(View.GONE);
			tabLayout2.setVisibility(View.VISIBLE);
			finishShowIv.setVisibility(View.VISIBLE);
			camera.stopPreview();
		} else {
			tabLayout2.setVisibility(View.GONE);
			finishShowIv.setVisibility(View.GONE);
			flashBtn.setVisibility(View.VISIBLE);
			tabLayout.setVisibility(View.VISIBLE);
			camera.startPreview();
		}
	}

	// 刷新闪光灯
	private void refreshFlashBtn() {
		boolean isOpen = PreferenceModel.instance(BPUICameraActivity.this)
				.getBoolean(BPUICameraConfig.CAMERA_FLASH_OPEN, false);
		if (isOpen) {
			flashBtn.setImageResource(M.drawable(BPUICameraActivity.this,
					"bpui_camera_flash_open"));
		} else {
			flashBtn.setImageResource(M.drawable(BPUICameraActivity.this,
					"bpui_camera_flash_close"));
		}
	}

}
