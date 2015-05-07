package com.dazzle.bigappleui.camera;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.Bitmap.Config;
import android.graphics.Matrix;
import android.util.Log;
import android.view.Surface;

import com.winupon.andframe.bigapple.bitmap.core.BitmapDecoder;
import com.winupon.andframe.bigapple.io.FileUtils;
import com.winupon.andframe.bigapple.utils.Validators;
import com.winupon.andframe.bigapple.utils.log.LogUtils;

/**
 * 相机工具类
 * 
 * @author xuan
 */
public abstract class BPUICameraUtils {
	private static final String TAG = "BPUICameraUtils";

	/**
	 * 打开相机功能
	 * 
	 * @param activity
	 * @param requestCode
	 */
	public static void openCamera(Activity activity, int requestCode,
			String saveFilePath) {
		Intent intent = new Intent();
		intent.setClass(activity, BPUICameraActivity.class);
		intent.putExtra(BPUICameraActivity.IMAGET_OUTPUT_PATH, saveFilePath);
		activity.startActivityForResult(intent, requestCode);
	}

	/**
	 * 保存图片-包内有效，只供内部使用
	 * 
	 * @param data
	 * @return
	 */
	static String saveImage(byte[] data, String toFileName, int degree) {
		if (null == data) {
			return null;
		}

		if (Validators.isEmpty(toFileName)) {
			toFileName = BPUICameraConfig.SDCARD_BIGAPPLEUI_CAMERA_DEFAULT;
		}

		try {
			createParentDirs(new File(toFileName));
			createParentDirs(new File(
					BPUICameraConfig.SDCARD_BIGAPPLEUI_CAMERA_TEMP));
			// 存放到临时目录
			com.winupon.andframe.bigapple.io.FileUtils.writeByteArrayToFile(
					new File(BPUICameraConfig.SDCARD_BIGAPPLEUI_CAMERA_TEMP),
					data, false);
			// 压缩调整到输出目录
			changeOppositeSizeMayDegree(
					BPUICameraConfig.SDCARD_BIGAPPLEUI_CAMERA_TEMP, toFileName,
					BPUICameraConfig.outputImageWidth,
					BPUICameraConfig.outputImageHeight, degree);
			// 删除临时文件
			FileUtils
					.deleteFileOrDirectoryQuietly(BPUICameraConfig.SDCARD_BIGAPPLEUI_CAMERA_TEMP);
		} catch (IOException e) {
			LogUtils.e(e.getMessage(), e);
			return null;
		}

		return toFileName;
	}

	/**
	 * 打印日志-包内有效，只供内部使用
	 * 
	 * @param msg
	 */
	static void debug(String tag, String msg) {
		if (BPUICameraConfig.DEBUG) {
			Log.d(tag, msg);
		}
	}

	/**
	 * 获取旋转角度-包内有效，只供内部使用
	 * 
	 * @param activity
	 * @return
	 */
	static int getPreviewDegree(Activity activity) {
		int degree = 0;
		// 获得手机的方向
		int rotation = activity.getWindowManager().getDefaultDisplay()
				.getRotation();
		// 根据手机的方向计算相机预览画面应该选择的角度
		switch (rotation) {
		case Surface.ROTATION_0:
			degree = 90;
			break;
		case Surface.ROTATION_90:
			degree = 0;
			break;
		case Surface.ROTATION_180:
			degree = 270;
			break;
		case Surface.ROTATION_270:
			degree = 180;
			break;
		}
		return degree;
	}

	/**
	 * 压缩图片
	 * 
	 * @param src
	 * @param dest
	 * @param newWidth
	 * @param newHeight
	 * @param degree
	 * @return
	 */
	static Bitmap changeOppositeSizeMayDegree(String src, String dest,
			int newWidth, int newHeight, int degree) {
		OutputStream out = null;
		Bitmap bitmap = null;
		Bitmap newBitmap = null;
		try {
			bitmap = BitmapDecoder.decodeSampledBitmapFromFile(src, newWidth,
					newHeight, Config.ARGB_8888);
			if (null == bitmap) {
				Log.e(TAG, "decodeSampledBitmapFromFile时图片返回null，估计是内存溢出，重启");
				return null;
			}

			// 调整图片角度
			newBitmap = rotateBitMap(bitmap, degree);

			File file = new File(dest);
			createParentDirs(file);
			out = new BufferedOutputStream(new FileOutputStream(file));
			newBitmap.compress(CompressFormat.JPEG, 90, out);// 保存到目的地
		} catch (Exception e) {
			Log.e(TAG, e.getMessage(), e);
			return null;
		} finally {
			try {
				if (null != out) {
					out.flush();
					out.close();
				}
			} catch (IOException e) {
				// Ignore
			}
		}

		return newBitmap;
	}

	/**
	 * 图片角度旋转
	 * 
	 * @param bp
	 * @param degree
	 * @return
	 */
	static Bitmap rotateBitMap(Bitmap sourceBitmap, int degree) {
		Bitmap newBitmap = null;
		Matrix matrix = new Matrix();
		matrix.reset();
		matrix.postRotate(degree);
		try {
			debug(TAG,
					"创建图片:w" + sourceBitmap.getWidth() + "h"
							+ sourceBitmap.getHeight());
			newBitmap = Bitmap.createBitmap(sourceBitmap, 0, 0,
					sourceBitmap.getWidth(), sourceBitmap.getHeight(), matrix,
					true);
		} catch (OutOfMemoryError e) {
			LogUtils.e(e.getMessage(), e);
		}

		if (null == newBitmap) {
			newBitmap = sourceBitmap;
		}
		if (sourceBitmap.isRecycled()) {
			sourceBitmap.recycle();
		}

		return newBitmap;
	}

	/**
	 * 如果父目录不存在，则创建之。
	 * 
	 * @param file
	 *            文件
	 */
	static void createParentDirs(File file) {
		File parentPath = file.getParentFile();
		if (!parentPath.exists() || !parentPath.isDirectory()) {
			parentPath.mkdirs();
		}
	}

}
