package com.dazzle.bigappleui.gifview;

import android.graphics.Bitmap;

/**
 * GIF图片的每一帧
 * 
 * @author xuan
 * @version $Revision: 1.0 $, $Date: 2013-9-27 下午6:50:27 $
 */
public class GifFrame {
    public Bitmap bitmap;
    public int delay;
    public GifFrame nextFrame = null;

    public GifFrame(Bitmap bitmap, int delay) {
        this.bitmap = bitmap;
        this.delay = delay;
    }

}
