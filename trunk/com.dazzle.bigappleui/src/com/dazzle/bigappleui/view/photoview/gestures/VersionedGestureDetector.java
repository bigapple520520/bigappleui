package com.dazzle.bigappleui.view.photoview.gestures;

import android.content.Context;
import android.os.Build;

/**
 * 各个系统版本的手势
 * 
 * @author xuan
 */
public final class VersionedGestureDetector {

	/**
	 * 根据系统版本，获取相应的手势操作
	 * 
	 * @param context
	 * @param listener
	 * @return
	 */
    public static GestureDetector newInstance(Context context, OnGestureListener listener) {
        final int sdkVersion = Build.VERSION.SDK_INT;
        
        GestureDetector detector = null;
        if (sdkVersion < Build.VERSION_CODES.ECLAIR) {
            detector = new CupcakeGestureDetector(context);
        } else if (sdkVersion < Build.VERSION_CODES.FROYO) {
            detector = new EclairGestureDetector(context);
        } else {
            detector = new FroyoGestureDetector(context);
        }

        detector.setOnGestureListener(listener);
        return detector;
    }

}