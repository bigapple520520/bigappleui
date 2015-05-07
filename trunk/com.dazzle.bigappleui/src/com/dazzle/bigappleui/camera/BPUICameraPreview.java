package com.dazzle.bigappleui.camera;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.graphics.ImageFormat;
import android.hardware.Camera;
import android.hardware.Camera.Size;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;
import android.view.SurfaceView;

import com.winupon.andframe.bigapple.utils.Validators;
import com.winupon.andframe.bigapple.utils.log.LogUtils;

/**
 * 自定义拍照界面
 * 
 * @author xuan
 * @version $Revision: 1.0 $, $Date: 2015-3-6 下午3:55:56 $
 */
public class BPUICameraPreview extends SurfaceView {
	public static final boolean DEBUG = true;

	/** 相机 */
	private Camera mCamera;
	/** 宿主界面对象 */
	private Activity mActivity;
	/** 手机的旋转角度 */
	private int mDisplayDegree;

	/** 等相机和surface初始化好后会执行该任务类 */
	private Runnable mInitRunnable;

	/** 屏幕的宽* */
	private int screenWidth;
	/** 屏幕的高 */
	private int screenHeight;

	public BPUICameraPreview(Context context) {
		super(context);
		init(context);
	}

	public BPUICameraPreview(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context);
	}

	public BPUICameraPreview(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init(context);
	}

	private void init(Context context) {
		mActivity = (Activity) context;
		DisplayMetrics dm = new DisplayMetrics();
		mActivity.getWindowManager().getDefaultDisplay().getMetrics(dm);
		screenWidth = dm.widthPixels;
		screenHeight = dm.heightPixels;

		initPreviewDegree();
		getHolder().setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
		getHolder().setFixedSize(480, 800); // 设置Surface分辨率
		getHolder().setKeepScreenOn(true);// 屏幕常亮
		getHolder().addCallback(new SurfaceCallback());// 为SurfaceView的句柄添加一个回调函数
	}

	/**
	 * SurfaceCall回调
	 * 
	 * @author xuan
	 */
	private final class SurfaceCallback implements Callback {
		@Override
		public void surfaceCreated(SurfaceHolder holder) {
			try {
				// 开始拍照时调用该方法
				mCamera = Camera.open(); // 打开摄像头
				mCamera.setPreviewDisplay(holder); // 设置用于显示拍照影像的SurfaceHolder对象
				mCamera.setDisplayOrientation(mDisplayDegree);
				mCamera.startPreview(); // 开始预览

				if (null != mInitRunnable) {
					mInitRunnable.run();
				}
			} catch (Exception e) {
				LogUtils.e(e.getMessage(), e);
			}
		}

		@Override
		public void surfaceChanged(SurfaceHolder holder, int format, int width,
				int height) {
			try {
				// 拍照状态变化时调用该方法
				Camera.Parameters parameters = mCamera.getParameters();
				// 设置图片格式
				parameters.setPictureFormat(ImageFormat.JPEG);
				// 设置照片质量
				parameters.setJpegQuality(80);

				// 预览规格
				Camera.Size preSize = getSupportedPictureSize(parameters, true);
				if (null != preSize) {
					parameters.setPreviewSize(preSize.width, preSize.height);
				}
				Camera.Size cPreSize = parameters.getPreviewSize();
				if (DEBUG) {
					LogUtils.d("--------------------current pre size:w/h("
							+ cPreSize.width + "/" + cPreSize.height + ")");
				}

				// 图片规格
				Camera.Size picSize = getSupportedPictureSize(parameters, false);
				if (null != picSize) {
					parameters.setPictureSize(picSize.width, picSize.height);
				}
				Camera.Size cPicSize = parameters.getPictureSize();
				if (DEBUG) {
					LogUtils.d("--------------------current pre size:w/h("
							+ cPicSize.width + "/" + cPicSize.height + ")");
				}

				// 聚焦一下
				mCamera.autoFocus(null);
			} catch (Exception e) {
				LogUtils.e(e.getMessage(), e);
			}
		}

		@Override
		public void surfaceDestroyed(SurfaceHolder holder) {
			// 停止拍照时调用该方法
			try {
				if (null != mCamera) {
					mCamera.stopPreview();
					mCamera.release(); // 释放照相机
					mCamera = null;
				}
			} catch (Exception e) {
				LogUtils.e(e.getMessage(), e);
			}
		}
	}

	// 获取旋转角度
	private void initPreviewDegree() {
		// 获得手机的方向
		int rotation = mActivity.getWindowManager().getDefaultDisplay()
				.getRotation();
		// 根据手机的方向计算相机预览画面应该选择的角度
		switch (rotation) {
		case Surface.ROTATION_0:
			mDisplayDegree = 90;
			break;
		case Surface.ROTATION_90:
			mDisplayDegree = 0;
			break;
		case Surface.ROTATION_180:
			mDisplayDegree = 270;
			break;
		case Surface.ROTATION_270:
			mDisplayDegree = 180;
			break;
		}
	}

	/** 获取拍照最佳规格 */
	private Camera.Size getSupportedPictureSize(Camera.Parameters parameters,
			boolean isPre) {
		List<Camera.Size> sizeList = parameters.getSupportedPictureSizes();
		if (Validators.isEmpty(sizeList)) {
			return null;
		}

		// 从小到大排序
		Collections.sort(sizeList, new SizeComparator());

		// 获取跟屏幕分辨率最接近的Size
		Camera.Size temp = null;
		for (Camera.Size s : sizeList) {
			temp = s;
			if (s.width * s.height >= screenWidth * screenHeight) {
				break;// 一旦匹配完成，就不往下比较了
			}
		}

		if (DEBUG) {
			for (Camera.Size s : sizeList) {
				if (isPre) {
					LogUtils.d("--------------------support pre size:w/h("
							+ s.width + "/" + s.height + ")");
				} else {
					LogUtils.d("--------------------support pic size:w/h("
							+ s.width + "/" + s.height + ")");
				}
			}
		}

		return temp;
	}

	/**
	 * Camera.Size从小到大排序规则
	 * 
	 * @author xuan
	 * @version $Revision: 1.0 $, $Date: 2015-3-6 上午9:59:10 $
	 */
	private static class SizeComparator implements Comparator<Camera.Size> {
		@Override
		public int compare(Size s1, Size s2) {
			int c1 = s1.width * s1.height;
			int c2 = s2.width * s2.height;
			return c1 == c2 ? 0 : (c1 > c2 ? 1 : -1);
		}
	}

	/**
	 * 获取相机对象
	 * 
	 * @return
	 */
	public Camera getCamera() {
		return mCamera;
	}

	/**
	 * 获取手机旋转角度
	 * 
	 * @return
	 */
	public int getDisplayDegree() {
		return mDisplayDegree;
	}

	/**
	 * 设置初始化回调
	 * 
	 * @param initRunnable
	 */
	public void setInitRunnable(Runnable initRunnable) {
		this.mInitRunnable = initRunnable;
	}

}
