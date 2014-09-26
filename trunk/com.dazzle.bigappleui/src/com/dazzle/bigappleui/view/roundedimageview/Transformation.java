package com.dazzle.bigappleui.view.roundedimageview;

import android.graphics.Bitmap;

/**
 * @author xuan
 * @version $Revision: 1.0 $, $Date: 2014-9-22 下午2:33:28 $
 */
public interface Transformation {
    /**
     * Transform the source bitmap into a new bitmap. If you create a new bitmap instance, you must call
     * {@link android.graphics.Bitmap#recycle()} on {@code source}. You may return the original if no transformation is
     * required.
     */
    Bitmap transform(Bitmap source);

    /**
     * Returns a unique key for the transformation, used for caching purposes. If the transformation has parameters
     * (e.g. size, scale factor, etc) then these should be part of the key.
     */
    String key();
}
