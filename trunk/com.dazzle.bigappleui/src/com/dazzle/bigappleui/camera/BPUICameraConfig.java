package com.dazzle.bigappleui.camera;

import android.os.Environment;

/**
 * 一些常用配置
 * 
 * @author xuan
 * 
 */
public class BPUICameraConfig {
	/** 相机功能是否开启debug调试 */
	public static final boolean DEBUG = true;
	/** 临时存放图片目录 */
	public static final String SDCARD_BIGAPPLEUI_CAMERA = Environment
			.getExternalStorageDirectory().getPath() + "/bigappleui/camera/";
	/** 临时存放图片地址 */
	public static final String SDCARD_BIGAPPLEUI_CAMERA_DEFAULT = Environment
			.getExternalStorageDirectory().getPath()
			+ "/bigappleui/camera/default.jpg";

	/** 图片临时存放地 */
	public static String SDCARD_BIGAPPLEUI_CAMERA_TEMP = Environment
			.getExternalStorageDirectory().getPath()
			+ "/bigappleui/camera/temp.jpg";

	/** 闪光灯设置 */
	public static final String CAMERA_FLASH_OPEN = "camera.flash.open";

	/** 限制图片输出的宽高 */
	public static int outputImageWidth = 1000;
	public static int outputImageHeight = 1000;

}
